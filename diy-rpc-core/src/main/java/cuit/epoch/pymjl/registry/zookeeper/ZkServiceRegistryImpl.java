package cuit.epoch.pymjl.registry.zookeeper;

import cuit.epoch.pymjl.registry.ServiceRegistry;
import cuit.epoch.pymjl.utils.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 19:50
 **/
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registryService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String serviceNode = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, serviceNode);
        log.info("注册成功");
    }
}
