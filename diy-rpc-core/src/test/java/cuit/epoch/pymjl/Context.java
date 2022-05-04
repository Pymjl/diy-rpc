package cuit.epoch.pymjl;

import cuit.epoch.pymjl.registry.zookeeper.ZkServiceRegistryImpl;
import cuit.epoch.pymjl.utils.CuratorUtils;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 11:13
 **/
@Slf4j
public class Context {


    @Test
    void test1() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostAddress();
        System.out.println(host);
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

    @Test
    void testList() {
        List<String> list = new ArrayList<>();
        System.out.println(list.isEmpty());
    }

    @Test
    void testMap() {
        Map<String, String> map = new HashMap<>();
        swap(map);
        map.forEach((k, v) -> System.out.println(k + v));
    }

    private void swap(Map<String, String> map) {
        map.put("Hello", "world");
    }

}
