package cuit.epoch.pymjl;

import cuit.epoch.pymjl.annotations.RpcScan;
import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.remote.transport.netty.server.NettyServer;
import cuit.epoch.pymjl.service.TestService;
import cuit.epoch.pymjl.service.impl.TestServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/27 11:12
 **/
@RpcScan(basePackage = "cuit.epoch.pymjl")
public class NettyServerMain {
    public static void main(String[] args) {
        // Register service via annotation
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyServer nettyRpcServer = (NettyServer) applicationContext.getBean("nettyServer");
        // Register service manually
        TestService testService = new TestServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test2").version("version2").service(testService).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
