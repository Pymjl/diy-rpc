package cuit.epoch.pymjl.nettydemo.client;

import cuit.epoch.pymjl.nettydemo.codec.NettyKryoDecoder;
import cuit.epoch.pymjl.nettydemo.codec.NettyKryoEncoder;
import cuit.epoch.pymjl.nettydemo.entity.RpcRequest;
import cuit.epoch.pymjl.nettydemo.entity.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 21:50
 **/
@Slf4j
public class NettyClient {
    private final String host;
    private final int port;
    private static final Bootstrap b;

    /**
     * 初始化Client
     *
     * @param host 服务器地址
     * @param port 服务器端口
     */
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 初始化Bootstrap等相关资源
     */
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //设置连接的超时时间，超过这个时间则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyKryoDecoder(RpcResponse.class));
                        socketChannel.pipeline().addLast(new NettyKryoEncoder(RpcRequest.class));
                        socketChannel.pipeline().addLast(new NettyClientHandler());

                    }
                });
    }

    /**
     * 发送请求
     *
     * @param rpcRequest 请求对象
     * @return 返回响应对象RpcResponse
     */
    public RpcResponse sendMessage(RpcRequest rpcRequest) {
        try {
            ChannelFuture f = b.connect(host, port).sync();
            log.info("client connect server success ==> {}:{}", host, port);
            Channel futureChannel = f.channel();
            log.info("client start send message");
            if (futureChannel != null) {
                futureChannel.writeAndFlush(rpcRequest).addListener(channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        log.info("client send message success ==> [{}]", rpcRequest);
                    } else {
                        log.error("send failed cause: ", channelFuture.cause());
                    }
                });
            }
            //阻塞等待服务器返回结果
            f.channel().closeFuture().sync();
            //获取返回结果
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("response");
            RpcResponse rpcResponse = futureChannel.attr(key).get();
            if (rpcResponse != null) {
                log.info("RpcResponse is [{}]", rpcResponse);
                return rpcResponse;
            } else {
                log.error("RpcResponse is Null");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
