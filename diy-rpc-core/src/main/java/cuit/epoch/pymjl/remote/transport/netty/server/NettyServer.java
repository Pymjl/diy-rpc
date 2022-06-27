package cuit.epoch.pymjl.remote.transport.netty.server;

import cuit.epoch.pymjl.config.CustomShutdownHook;
import cuit.epoch.pymjl.config.RpcServiceConfig;
import cuit.epoch.pymjl.factory.SingletonFactory;
import cuit.epoch.pymjl.registry.provider.ServiceProvider;
import cuit.epoch.pymjl.registry.provider.impl.ZkServiceProviderImpl;
import cuit.epoch.pymjl.remote.transport.netty.codec.MessageDecoder;
import cuit.epoch.pymjl.remote.transport.netty.codec.MessageEncoder;
import cuit.epoch.pymjl.utils.RuntimeUtil;
import cuit.epoch.pymjl.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * NettyServer服务端，用于接收客户端的请求，并返回对应的响应
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/24 21:16
 **/
@Slf4j
@Component
public class NettyServer {
    /**
     * 服务端端口号
     */
    public static final int PORT = 9998;

    /**
     * 服务提供者，用于注册发布服务等
     */
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    /**
     * 注册服务
     *
     * @param rpcServiceConfig rpc服务配置
     */
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @SneakyThrows
    public void start() {
        //在项目刚开始启动的时候先清除之前存在的节点信息
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        //获取到本地的主机地址
        String host = InetAddress.getLocalHost().getHostAddress();
        //bossGroup用于处理客户端的连接时间，线程数1
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //workGroup处理网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //独立的线程池用于处理业务逻辑，用于将IO线程和业务处理逻辑线程分开
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("service-handler-group", false)
        );
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 30 秒之内没有收到客户端请求的话就关闭连接
                            ChannelPipeline p = ch.pipeline();
                            //Netty自带的心跳处理器，应用层自定义，监听读超时
                            p.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                            p.addLast(new MessageEncoder());
                            p.addLast(new MessageDecoder());
                            p.addLast(serviceHandlerGroup, new NettyServerHandler());
                        }
                    });

            // 绑定端口，同步等待绑定成功
            ChannelFuture f = b.bind(host, PORT).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }

}
