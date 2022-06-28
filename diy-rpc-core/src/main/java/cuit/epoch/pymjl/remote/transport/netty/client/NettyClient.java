package cuit.epoch.pymjl.remote.transport.netty.client;

import cuit.epoch.pymjl.enums.CompressTypeEnum;
import cuit.epoch.pymjl.enums.SerializationTypeEnum;
import cuit.epoch.pymjl.factory.SingletonFactory;
import cuit.epoch.pymjl.registry.ServiceDiscovery;
import cuit.epoch.pymjl.remote.constants.RpcConstants;
import cuit.epoch.pymjl.remote.entity.RpcMessage;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.remote.entity.RpcResponse;
import cuit.epoch.pymjl.remote.transport.RpcRequestTransport;
import cuit.epoch.pymjl.remote.transport.netty.codec.MessageDecoder;
import cuit.epoch.pymjl.remote.transport.netty.codec.MessageEncoder;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/26 0:06
 **/
@Slf4j
public class NettyClient implements RpcRequestTransport {
    /**
     * 负责从zookeeper中获取服务的远程地址
     */
    private final ServiceDiscovery serviceDiscovery;

    /**
     * 处理请求
     */
    private final UnprocessedRequests unprocessedRequests;

    /**
     * 通道提供者
     */
    private final ChannelProvider channelProvider;

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyClient() {
        // 初始化资源
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //  连接超时时间，如果超过此时间或无法建立连接，则连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 如果 5 秒内没有数据发送到服务器，则触发心跳事件，发送心跳请求
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new MessageEncoder());
                        p.addLast(new MessageDecoder());
                        p.addLast(new NettyClientHandler());
                    }
                });
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zookeeper");
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * 连接服务器并获取通道，以便您可以向服务器发送rpc消息
     *
     * @param inetSocketAddress server address
     * @return the channel
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 创建异步任务，这样主线程就不用阻塞等待服务器的响应,仅需要调用get方法就可以得到服务端的响应
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // 获取远程服务器的地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.findService(rpcRequest);
        // 与服务器建立连接，再得到相关通道
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            //将对应的异步请求添加到map中
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            // 构造对应的请求数据
            RpcMessage rpcMessage = RpcMessage.builder().data(rpcRequest)
                    //TODO 将序列化方式修改为配制文件的方式，避免硬编码
                    .codec(SerializationTypeEnum.HESSIAN.getCode())
                    .compress(CompressTypeEnum.GZIP.getCode())
                    .messageType(RpcConstants.REQUEST_TYPE).build();
            //发送请求
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }

    /**
     * 与服务器建立连接，再得到通道
     *
     * @param inetSocketAddress inet套接字地址
     * @return {@code Channel}
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }
}
