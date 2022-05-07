package cuit.epoch.pymjl.spi;

import cuit.epoch.pymjl.annotations.MySpi;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模仿Dubbo自定义扩展机制
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 15:12
 **/
@Slf4j
public class ExtensionLoader<T> {
    /**
     * SPI配置文件根目录
     */
    private static final String SERVICE_DIRECTORY = "META-INF/diy-rpc/";

    /**
     * 本地缓存，Dubbo会先通过getExtensionLoader方法从缓存中获取一个ExtensionLoader
     * 若缓存未命中，则会生成一个新的实例
     */
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER_MAP = new ConcurrentHashMap<>();

    /**
     * 目标扩展类的字节码和实例对象
     */
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    /**
     * 需要加载的扩展类类别
     */
    private final Class<?> type;

    /**
     * 本地缓存
     */
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 扩展类实例对象，key为配置文件中的key，value为实例对象的全限定名称
     */
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    public ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    /**
     * 得到扩展加载程序
     *
     * @param type 要扩展的接口，必须被MySpi标记
     * @return {@code ExtensionLoader<S>}
     */
    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
        //判断type是否为null
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        //如果不是接口
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface.");
        }
        //判断是否被MySpi标记
        if (type.getAnnotation(MySpi.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated by @MySpi");
        }
        //先从缓存中获取扩展加载器，如果未命中，则创建
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADER_MAP.get(type);
        if (extensionLoader == null) {
            //未命中则创建，并放入缓存
            EXTENSION_LOADER_MAP.putIfAbsent(type, new ExtensionLoader<>(type));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADER_MAP.get(type);
        }
        return extensionLoader;
    }

    /**
     * 得到扩展类对象实例
     *
     * @param name 配置名字
     * @return {@code T}
     */
    public T getExtension(String name) {
        //检查参数
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Extension name should not be null or empty.");
        }
        //先从缓存中获取，如果未命中，新建
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        //如果Holder还未持有目标对象，则为其创建一个单例对象
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    /**
     * 通过扩展类字节码创建实例对象
     *
     * @param name 名字
     * @return {@code T}
     */
    private T createExtension(String name) {
        //从文件中加载所有类型为 T 的扩展类并按名称获取特定的扩展类
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("No such extension of name " + name);
        }
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        return instance;
    }

    /**
     * 获取所有扩展类
     *
     * @return {@code Map<String, Class<?>>}
     */
    private Map<String, Class<?>> getExtensionClasses() {
        //从缓存中获取已经加载的扩展类
        Map<String, Class<?>> classes = cachedClasses.get();
        //双重检查
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = new HashMap<>();
                    //从配置文件中加载所有扩展类
                    loadDirectory(classes);
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    /**
     * 从配置目录中加载所有扩展类
     *
     * @param extensionsClasses 扩展类的K,V键值对
     */
    private void loadDirectory(Map<String, Class<?>> extensionsClasses) {
        String fileName = ExtensionLoader.SERVICE_DIRECTORY + type.getName();
        try {
            //获取配置文件的资源路径
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionsClasses, classLoader, resourceUrl);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 通过Url加载资源
     *
     * @param extensionClasses 扩展类，key为配置文件中的key，Value为实现类的全限定名称
     * @param classLoader      类加载器
     * @param resourceUrl      资源url
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), StandardCharsets.UTF_8))) {
            String line;
            //读取文件中的每一行数据
            while ((line = reader.readLine()) != null) {
                //先排除配置文件中的注释
                final int noteIndex = line.indexOf('#');
                //我们应该忽略掉注释后的内容
                if (noteIndex > 0) {
                    line = line.substring(0, noteIndex);
                }
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        final int keyIndex = line.indexOf('=');
                        String key = line.substring(0, keyIndex).trim();
                        String value = line.substring(keyIndex + 1).trim();
                        if (key.length() > 0 && value.length() > 0) {
                            Class<?> clazz = classLoader.loadClass(value);
                            extensionClasses.put(key, clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }


}
