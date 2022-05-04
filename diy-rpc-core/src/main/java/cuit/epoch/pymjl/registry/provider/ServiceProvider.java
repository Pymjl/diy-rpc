package cuit.epoch.pymjl.registry.provider;

import cuit.epoch.pymjl.config.RpcServiceConfig;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 9:15
 **/
public interface ServiceProvider {
    /**
     * 添加服务
     *
     * @param rpcServiceConfig rpc服务配置
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * 根据服务名称获取服务
     *
     * @param rpcServiceName rpc服务名称
     * @return {@code Object}
     */
    Object getService(String rpcServiceName);

    /**
     * 发布服务
     *
     * @param rpcServiceConfig rpc服务配置
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

}
