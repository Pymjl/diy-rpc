package cuit.epoch.pymjl;

import cuit.epoch.pymjl.registry.zookeeper.ZkServiceRegistryImpl;
import cuit.epoch.pymjl.utils.CuratorUtils;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 11:13
 **/
@Slf4j
public class Context {


    @Test
    void test1() {
        log.info("wewew");
    }

    @Test
    void test2() {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        System.out.println(address.toString());
    }

    @Test
    void testZk() {
        ZkServiceRegistryImpl zkServiceRegistry = new ZkServiceRegistryImpl();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        zkServiceRegistry.registryService("cuit.epoch.service.HelloService#HelloServiceImpl#1.0",
                address);
    }

}
