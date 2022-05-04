package cuit.epoch.pymjl.registry.zookeeper;

import cuit.epoch.pymjl.enums.RpcErrorMessageEnum;
import cuit.epoch.pymjl.exception.RpcException;
import cuit.epoch.pymjl.loadbalance.LoadBalance;
import cuit.epoch.pymjl.registry.ServiceDiscovery;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import cuit.epoch.pymjl.utils.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 19:49
 **/
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    @Override
    public InetSocketAddress findService(RpcRequest rpcRequest) {
        //获取服务名称
        String serviceName = rpcRequest.getRpcServiceName();
        //获取Curator客户端
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        //获取服务的所有子节点
        List<String> childrenNodes = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        if (childrenNodes == null || childrenNodes.isEmpty()) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, serviceName);
        }
        //负载均衡
        String serviceUrl = loadBalance.findServiceByRule(childrenNodes, rpcRequest);
        log.info("成功发现服务地址==>[{}]", serviceUrl);
        String[] socketAddressArray = serviceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
