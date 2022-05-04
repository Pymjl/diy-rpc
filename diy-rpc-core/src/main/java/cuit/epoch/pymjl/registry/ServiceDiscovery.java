package cuit.epoch.pymjl.registry;

import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.spi.MySpi;

import java.net.InetSocketAddress;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 19:44
 **/
@MySpi
public interface ServiceDiscovery {
    /**
     * 服务发现，获取远程服务地址
     *
     * @param rpcRequest 请求对象
     * @return {@code InetSocketAddress} 远程服务地址
     */
    InetSocketAddress findService(RpcRequest rpcRequest);
}
