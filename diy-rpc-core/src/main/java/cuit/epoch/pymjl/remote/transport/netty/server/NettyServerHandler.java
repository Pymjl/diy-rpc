package cuit.epoch.pymjl.remote.transport.netty.server;

import cuit.epoch.pymjl.enums.CompressTypeEnum;
import cuit.epoch.pymjl.enums.RpcResponseCodeEnum;
import cuit.epoch.pymjl.enums.SerializationTypeEnum;
import cuit.epoch.pymjl.factory.SingletonFactory;
import cuit.epoch.pymjl.remote.constants.RpcConstants;
import cuit.epoch.pymjl.remote.entity.RpcMessage;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.remote.entity.RpcResponse;
import cuit.epoch.pymjl.remote.handler.RpcRequestHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义服务端的ChannelHandler来处理客户端发送的数据
 * <p>
 * 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。详见《Netty进阶之路 跟着案例学 Netty》
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/24 21:16
 **/
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final RpcRequestHandler rpcRequestHandler;

    public NettyServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof RpcMessage) {
                log.info("server receive msg: [{}] ", msg);
                byte messageType = ((RpcMessage) msg).getMessageType();
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setCodec(SerializationTypeEnum.HESSIAN.getCode());
                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());

                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                    rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                    rpcMessage.setData(RpcConstants.PONG);
                } else {
                    RpcRequest rpcRequest = (RpcRequest) ((RpcMessage) msg).getData();
                    //执行对应的rpc接口，并返回对应的结果
                    Object result = rpcRequestHandler.handle(rpcRequest);
                    log.info(String.format("server get result: %s", result.toString()));
                    rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                        rpcMessage.setData(rpcResponse);
                    } else {
                        RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCodeEnum.FAIL);
                        rpcMessage.setData(rpcResponse);
                        log.error("not writable now, message dropped");
                    }
                }
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } finally {
            //确保释放ByteBuf，否则可能会出现内存泄漏
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 处理心跳超时事件，当服务端发生读超时时，就断开连接
     *
     * @param ctx ctx
     * @param evt evt
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
