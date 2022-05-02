package cuit.epoch.pymjl.registry;

import java.net.InetSocketAddress;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 19:44
 **/
public interface ServiceDiscovery {
    /**
     * 服务发现，获取远程服务地址
     *
     * @param rpcServiceName 完整的rpc服务名称
     * @return {@code InetSocketAddress} 远程服务地址
     */
    InetSocketAddress findService(String rpcServiceName);
}
