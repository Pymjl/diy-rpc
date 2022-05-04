package cuit.epoch.pymjl.registry.provider.impl;

import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.enums.RpcErrorMessageEnum;
import cuit.epoch.pymjl.exception.RpcException;
import cuit.epoch.pymjl.registry.ServiceRegistry;
import cuit.epoch.pymjl.registry.provider.ServiceProvider;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 9:24
 **/
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {
    private final ServiceRegistry serviceRegistry;
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredServices;

    public ZkServiceProviderImpl() {
        this.serviceRegistry = ExtensionLoader
                .getExtensionLoader(ServiceRegistry.class).getExtension("zookeeper");
        this.serviceMap = new ConcurrentHashMap<>();
        this.registeredServices = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String serviceName = rpcServiceConfig.getRpcServiceName();
        //判断服务是否已被添加
        if (registeredServices.contains(serviceName)) {
            log.warn("[{}]服务已经被添加", serviceName);
        } else {
            registeredServices.add(serviceName);
            serviceMap.put(serviceName, rpcServiceConfig.getService());
            log.info("Add service: {} and interfaces:{}", serviceName,
                    rpcServiceConfig.getService().getClass().getInterfaces());
        }
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            //TODO 此时端口为写死的8080，后续改为动态的服务端口
            serviceRegistry.registryService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, 8080));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
