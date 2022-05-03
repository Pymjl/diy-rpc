package cuit.epoch.pymjl.registry;

import cuit.epoch.pymjl.enums.RpcConfigEnum;
import cuit.epoch.pymjl.registry.zookeeper.ZkServiceDiscoveryImpl;
import cuit.epoch.pymjl.registry.zookeeper.ZkServiceRegistryImpl;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.utils.PropertiesFileUtil;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 20:40
 **/
public class Main {
    @Test
    void testReadPropertiesFile() {
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        System.out.println(properties.toString());
    }

    @Test
    void testRegisterAndFindServices() {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName("HelloService");
        rpcRequest.setVersion("v1");
        rpcRequest.setTargetImpl("HelloServiceImpl");
        String serviceName = rpcRequest.getServiceName();
        System.out.println("ServiceName: " + serviceName);

        InetSocketAddress address1 = new InetSocketAddress("127.0.0.1", 8080);
        InetSocketAddress address2 = new InetSocketAddress("127.0.0.2", 8080);
        InetSocketAddress address3 = new InetSocketAddress("127.0.0.3", 8080);
        InetSocketAddress address4 = new InetSocketAddress("127.0.0.4", 8080);
        InetSocketAddress address5 = new InetSocketAddress("127.0.0.5", 8080);

        //服务注册
        ServiceRegistry serviceRegistry = new ZkServiceRegistryImpl();
        serviceRegistry.registryService(serviceName, address1);
        serviceRegistry.registryService(serviceName, address2);
        serviceRegistry.registryService(serviceName, address3);
        serviceRegistry.registryService(serviceName, address4);
        serviceRegistry.registryService(serviceName, address5);

        //服务发现
        ServiceDiscovery serviceDiscovery = new ZkServiceDiscoveryImpl();
        System.out.println(serviceDiscovery.findService(rpcRequest));


    }
}
