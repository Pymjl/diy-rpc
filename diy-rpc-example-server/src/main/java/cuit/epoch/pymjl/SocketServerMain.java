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
        //创建对应rpc服务的实例化对象
        HelloService helloService = new HelloServiceImpl();
        //启动服务器
        SocketServer socketServer = new SocketServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        //将rpc实例化对象放进配置对象中
        rpcServiceConfig.setService(helloService);
        //通过配置对象向注册中心注册服务，并且将实例化对象放进本地缓存
        socketServer.registerService(rpcServiceConfig);
        //启动Server端
        socketServer.start();
    }

}
