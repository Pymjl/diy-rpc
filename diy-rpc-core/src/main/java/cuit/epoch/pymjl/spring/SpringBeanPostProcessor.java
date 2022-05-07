package cuit.epoch.pymjl.spring;

import cuit.epoch.pymjl.annotations.RpcReference;
import cuit.epoch.pymjl.annotations.RpcService;
import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.factory.SingletonFactory;
import cuit.epoch.pymjl.proxy.RpcClientProxy;
import cuit.epoch.pymjl.registry.provider.ServiceProvider;
import cuit.epoch.pymjl.registry.provider.impl.ZkServiceProviderImpl;
import cuit.epoch.pymjl.remote.transport.RpcRequestTransport;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/8 0:16
 **/
@Slf4j
public class SpringBeanPostProcessor implements BeanPostProcessor {
    private final ServiceProvider serviceProvider;
    private final RpcRequestTransport rpcClient;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        //TODO 传输方式可以修改为配置文件的方式动态配置
        this.rpcClient = ExtensionLoader.getExtensionLoader(RpcRequestTransport.class).getExtension("socket");
    }

    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // 获取 RpcService 注解
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            // 构建 RpcServiceProperties
            RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                    .group(rpcService.group())
                    .version(rpcService.version())
                    .service(bean).build();
            //向注册中心发布服务
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }

    /**
     * 通过反射将RpcReference所引用的属性赋值
     *
     * @param bean     对象
     * @param beanName bean名字
     * @return {@code Object}
     * @throws BeansException 异常
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                        .group(rpcReference.group())
                        .version(rpcReference.version()).build();
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceConfig);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return bean;
    }
}
