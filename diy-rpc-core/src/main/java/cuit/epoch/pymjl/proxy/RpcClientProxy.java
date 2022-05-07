package cuit.epoch.pymjl.proxy;

import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.enums.RpcErrorMessageEnum;
import cuit.epoch.pymjl.enums.RpcResponseCodeEnum;
import cuit.epoch.pymjl.exception.RpcException;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.remote.entity.RpcResponse;
import cuit.epoch.pymjl.remote.transport.RpcRequestTransport;
import cuit.epoch.pymjl.remote.transport.socket.SocketClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/7 17:31
 **/
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private static final String INTERFACE_NAME = "interfaceName";

    /**
     * 发起RPC请求的客户端，有Netty和Socket两种对象
     */
    private final RpcRequestTransport rpcRequestTransport;

    /**
     * rpc服务配置
     */
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RpcServiceConfig();
    }


    /**
     * 获得代理对象
     *
     * @param clazz clazz
     * @return {@code T}
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    /**
     * 代理客户端，向服务端发请求，让客户端能够像调用本地方法一样调用远程方法
     *
     * @param proxy  动态生成的代理对象
     * @param method 要执行的方法
     * @param args   方法参数
     * @return {@code Object}
     * @throws Throwable throwable
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("开始代理执行方法：[{}]", method.getName());
        //构造RPC请求对象
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        RpcResponse<Object> response = null;
        //根据不同的实现客户端做出不同的调用
        if (rpcRequestTransport instanceof SocketClient) {
            //发送请求
            response = (RpcResponse<Object>) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }
        //TODO 添加Netty客户端相关的逻辑

        //对返回结果进行校验
        check(response, rpcRequest);
        return response.getData();
    }

    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
