package cuit.epoch.pymjl.loadbalance.loadbalancer;

import cuit.epoch.pymjl.loadbalance.LoadBalance;
import cuit.epoch.pymjl.remote.entity.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * TODO 先暂时直接随即选择一个服务节点，后续模仿Dubbo实现根据权重进行随机选择
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 12:22
 **/
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String doSelect(List<String> serviceNodes, RpcRequest rpcRequest) {
        Random random = new Random();
        return serviceNodes.get(random.nextInt(serviceNodes.size()));
    }
}
