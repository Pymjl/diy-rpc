package cuit.epoch.pymjl.nettydemo.client;

import cuit.epoch.pymjl.nettydemo.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.log4j.Log4j2;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 22:04
 **/
@Log4j2
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            RpcResponse response = (RpcResponse) msg;
            log.info("handler client receive response from server, response={}", response.toString());
            //声明一个AttributeKey对象
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("response");
            ctx.channel().attr(key).set(response);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception", cause);
        ctx.close();
    }
}
