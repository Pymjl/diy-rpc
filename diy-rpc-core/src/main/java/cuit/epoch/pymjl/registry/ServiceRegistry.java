package cuit.epoch.pymjl.registry;

import cuit.epoch.pymjl.annotations.MySpi;

import java.net.InetSocketAddress;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/2 19:41
 **/
@MySpi
public interface ServiceRegistry {
    /**
     * 注册服务到注册中心
     *
     * @param rpcServiceName    rpc服务名称(接口名称+目标实现类+版本)
     * @param inetSocketAddress 远程服务地址
     */
    void registryService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
