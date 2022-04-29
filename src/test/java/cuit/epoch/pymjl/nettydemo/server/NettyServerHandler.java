package cuit.epoch.pymjl.nettydemo.server;

import cuit.epoch.pymjl.nettydemo.entity.RpcRequest;
import cuit.epoch.pymjl.nettydemo.entity.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 22:26
 **/
@Log4j2
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final AtomicInteger atomicInteger = new AtomicInteger(1);

    /**
     * 接收客户端发送的请求
     * @param ctx 上下文
     * @param msg 消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcRequest rpcRequest = (RpcRequest) msg;
            log.info("server receive msg: [{}] ,times:[{}]", rpcRequest, atomicInteger.getAndIncrement());
            RpcResponse messageFromServer = RpcResponse.builder().message("message from server").build();
            ChannelFuture f = ctx.writeAndFlush(messageFromServer);
            f.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception", cause);
        ctx.close();
    }

}
