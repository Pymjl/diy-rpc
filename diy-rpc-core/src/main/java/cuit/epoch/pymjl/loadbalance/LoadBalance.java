package cuit.epoch.pymjl.loadbalance;

import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.spi.SPI;

import java.util.List;

/**
 * 负载均衡的策略
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 11:32
 **/
@SPI
public interface LoadBalance {
    /**
     * 通过负载均衡的策略发现服务
     *
     * @param serviceNodes 服务地址节点列表 eg: /127.0.0.1:8080
     * @param rpcRequest   rpc请求
     * @return {@code String} 服务的url
     */
    default String findServiceByRule(List<String> serviceNodes, RpcRequest rpcRequest) {
        if (serviceNodes == null || serviceNodes.isEmpty()) {
            return null;
        } else if (serviceNodes.size() == 1) {
            return serviceNodes.get(0);
        } else {
            return doSelect(serviceNodes, rpcRequest);
        }
    }

    /**
     * 根据不同的负载均衡策略对服务的节点做选择，
     * 实现的负载均衡规则需要继承该类并实现doSelect方法
     *
     * @param serviceNodes 服务节点
     * @param rpcRequest   rpc请求
     * @return {@code String}
     */
    String doSelect(List<String> serviceNodes, RpcRequest rpcRequest);
}
