package cuit.epoch.pymjl;

import cuit.epoch.pymjl.annotations.RpcScan;
import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.proxy.RpcClientProxy;
import cuit.epoch.pymjl.remote.transport.RpcRequestTransport;
import cuit.epoch.pymjl.remote.transport.socket.SocketClient;
import cuit.epoch.pymjl.service.HelloService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/11 22:09
 **/
public class SocketClientMain {
    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport = new SocketClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        helloService.sayHello("Hello Socket Service");
    }
}
