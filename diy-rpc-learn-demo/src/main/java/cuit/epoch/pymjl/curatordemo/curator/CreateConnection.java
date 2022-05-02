package cuit.epoch.pymjl.curatordemo.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 20:37
 **/
@Slf4j
public class CreateConnection {
    private static final String PATH = "/test/t2";
    private static final String ROOT_PATH = "/test";

    public static void main(String[] args) throws Exception {
        /*
          重试策略，重试3次且每次多1s
         */
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                // 若配置集群则是 ip1:port, ip2:post, ip3:port(,隔开)
                .connectString("106.12.167.1:2181")
                // 超时，也是心跳时间
                .sessionTimeoutMs(50000)
                .retryPolicy(exponentialBackoffRetry)
                .build();
        zkClient.start();
        log.info("连接成功");

        //查询子节点
        log.info("开始查询子节点......");
        List<String> s = zkClient.getChildren().forPath(ROOT_PATH);
        for (String node : s) {
            System.out.println(node);
        }

        //创建节点
        log.info("开始创建新的持久节点");
        //父节点不存在时递归创建，因为一次本来只能一个节点
        zkClient.create().creatingParentContainersIfNeeded()
                // 创建的节点类型，共有七种，注意临时节点不能有子节点
                .withMode(CreateMode.PERSISTENT)
                .forPath(PATH, "Hello world".getBytes(StandardCharsets.UTF_8));

        //查询子节点
        log.info("开始查询创建后的子节点列表......");
        List<String> s2 = zkClient.getChildren().forPath(ROOT_PATH);
        for (String node : s2) {
            System.out.println(node);
        }

        //查询节点内容
        log.info("开始查询创建后的节点内容");
        byte[] bytes = zkClient.getData().forPath(PATH);
        System.out.println(new String(bytes));

        //修改节点内容
        zkClient.setData().forPath(PATH, "update Hello world".getBytes(StandardCharsets.UTF_8));

        //查询节点内容
        log.info("开始查询更新后的子节点内容");
        byte[] byte01 = zkClient.getData().forPath(PATH);
        System.out.println(new String(byte01));

        //删除节点
        log.info("开始删除节点");
        Stat stat = new Stat();
        zkClient.getData().storingStatIn(stat).forPath(PATH);
        zkClient.delete().withVersion(stat.getVersion()).forPath(PATH);

        //查询子节点
        log.info("开始查询删除节点后的子节点列表......");
        List<String> s1 = zkClient.getChildren().forPath(ROOT_PATH);
        for (String node : s1) {
            System.out.println(node);
        }
    }
}
