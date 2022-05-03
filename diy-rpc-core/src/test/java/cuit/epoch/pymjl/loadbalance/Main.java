package cuit.epoch.pymjl.loadbalance;

import cuit.epoch.pymjl.loadbalance.loadbalancer.RandomLoadBalance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 12:29
 **/
public class Main {
    @Test
    void testLoadBalance() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        LoadBalance loadBalance = new RandomLoadBalance();
        System.out.println(loadBalance.findServiceByRule(list, null));
        System.out.println(loadBalance.findServiceByRule(list, null));
        System.out.println(loadBalance.findServiceByRule(list, null));
    }
}
