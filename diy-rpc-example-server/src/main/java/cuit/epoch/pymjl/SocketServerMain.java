package cuit.epoch.pymjl;

import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.remote.transport.socket.SocketServer;
import cuit.epoch.pymjl.service.HelloService;
import cuit.epoch.pymjl.service.impl.HelloServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/11 21:57
 **/
public class SocketServerMain {
    public static void main(String[] args) {
        //创建服务
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        //手动注册服务
        rpcServiceConfig.setService(helloService);
        socketServer.registerService(rpcServiceConfig);
        //启动Server端
        socketServer.start();
    }

}
