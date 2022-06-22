package cuit.epoch.pymjl.remote.transport.socket;

import cuit.epoch.pymjl.config.CustomShutdownHook;
import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.factory.SingletonFactory;
import cuit.epoch.pymjl.registry.provider.ServiceProvider;
import cuit.epoch.pymjl.registry.provider.impl.ZkServiceProviderImpl;
import cuit.epoch.pymjl.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 11:48
 **/
@Slf4j
public class SocketServer {
    /**
     * 线程池
     */
    private final ExecutorService threadPool;

    /**
     * 服务提供者，提供向注册中心注册服务的接口
     */
    private final ServiceProvider serviceProvider;

    public SocketServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        //TODO 这里可以通过SPI机制加载不同的服务注册与发现的提供者，暂时只有zookeeper一种实现，则直接用单例工厂
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 向注册中心注册服务
     *
     * @param rpcServiceConfig rpc服务配置
     */
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            //TODO 端口暂时写死为8080
            server.bind(new InetSocketAddress(host, 8080));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            log.info("服务端启动成功");
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRequestHandler(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
