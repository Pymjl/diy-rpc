package cuit.epoch.pymjl.remote.handler;

import cuit.epoch.pymjl.exception.RpcException;
import cuit.epoch.pymjl.factory.SingletonFactory;
import cuit.epoch.pymjl.registry.provider.ServiceProvider;
import cuit.epoch.pymjl.registry.provider.impl.ZkServiceProviderImpl;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 11:29
 **/
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 处理rpcRequest：调用对应的方法，然后返回方法结果
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 执行远程调用的方法
     *
     * @param rpcRequest 请求对象
     * @param service    服务
     * @return 方法结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            //通过反射获取要调用的方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            //执行方法
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("服务端:[{}] 成功执行方法:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
