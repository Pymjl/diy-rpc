package cuit.epoch.pymjl.curatordemo;

import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/22 16:02
 **/
public class Main {
    public static void main(String[] args) throws Exception {
        BaseZookeeper baseZookeeper = new BaseZookeeper();
        baseZookeeper.connectZookeeper("106.12.167.1:2181");
        List<String> children = baseZookeeper.getChildren("/");
        System.out.println(children);
        System.out.println(baseZookeeper.getData("/my-rpc"));
    }
}
