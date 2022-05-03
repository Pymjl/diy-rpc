package cuit.epoch.pymjl.spi;

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
     *
     */
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    /**
     * 需要加载的扩展类类别
     */
    private final Class<?> type;

    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

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

    //TODO 完善创建扩展类实例的方法
    private T createExtension(String name) {
        return null;
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
     * @param extensionsClasses 扩展类
     */
    private void loadDirectory(Map<String, Class<?>> extensionsClasses) {
        String fileName = ExtensionLoader.SERVICE_DIRECTORY + type.getName();
        //TODO 后续的代码

    }


}
