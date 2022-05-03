package cuit.epoch.pymjl.utils;


import cuit.epoch.pymjl.enums.RpcConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 20:10
 **/
@Slf4j
public final class CuratorUtils {

    /**
     * 连接Zookeeper重试策略，每次重试后休眠增加的时间
     */
    private static final int BASE_SLEEP_TIME = 1000;

    /**
     * 连接Zookeeper重试策略，重试三次，每次休眠时间增加BASE_SLEEP_TIME
     */
    private static final int MAX_RETRIES = 3;

    /**
     * 注册服务的根节点
     */
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";

    /**
     * Curator客户端
     */
    private static CuratorFramework zkClient;

    /**
     * 默认Zookeeper的主机名和端口，可通过配置文件配置
     */
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    /**
     * 已经注册的服务信息，本地缓存
     */
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();

    /**
     * 已经存在的节点，本地缓存
     */
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();


    private CuratorUtils() {
    }

    /**
     * 创建持久节点，注意临时节点没有子节点
     *
     * @param zkClient Curator客户端
     * @param path     节点的路径
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            //先从本地缓存判断该节点是否存在
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("The node already exists. The node is:[{}]", path);
            } else {
                /* 如果不存在则创建节点
                 * eg: /my-rpc/github.javaguide.HelloService/127.0.0.1:9999
                 * creatingParentsIfNeeded当父节点不存在是递归创建父节点
                 * CreateMode.PERSISTENT 节点类型，持久节点
                 */
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The node was created successfully. The node is:[{}]", path);
            }
            //将已注册节点加入本地缓存
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            log.error("create persistent node for path [{}] fail", path);
        }
    }


    /**
     * 获取节点下的所有子节点
     *
     * @param zkClient       Curator客户端
     * @param rpcServiceName 服务名称
     * @return 该节点下的所有子节点
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        //先查询本地缓存
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result = null;
        //拼接节点路径
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            //查询zookeeper，并将结果放入本地缓存
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            log.error("get children nodes for path [{}] fail", servicePath);
        }
        return result;
    }


    /**
     * 清除服务子节点
     *
     * @param zkClient          Curator客户端
     * @param inetSocketAddress 服务ip+端口
     */
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                if (p.endsWith(inetSocketAddress.toString())) {
                    zkClient.delete().forPath(p);
                }
            } catch (Exception e) {
                log.error("clear registry for path [{}] fail", p);
            }
        });
        log.info("All registered services on the server are cleared:[{}]", REGISTERED_PATH_SET.toString());
    }

    /**
     * 获取Curator客户端
     *
     * @return CuratorFramework
     */
    public static CuratorFramework getZkClient() {
        // 检查用户是否通过配置文件设置了zookeeper地址
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String zookeeperAddress = properties != null && properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue()) != null ? properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue()) : DEFAULT_ZOOKEEPER_ADDRESS;
        // if zkClient has been started, return directly
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        // Retry strategy. Retry 3 times, and will increase the sleep time between retries.
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                // the server to connect to (can be a server list)
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        try {
            // wait 30s until connect to the zookeeper
            if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Time out waiting to connect to ZK!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkClient;
    }

    /**
     * Registers to listen for changes to the specified node
     *
     * @param rpcServiceName rpc service name eg:github.javaguide.HelloServicetest2version
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

}
