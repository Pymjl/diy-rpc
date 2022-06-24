package cuit.epoch.pymjl.nettyidledemo.handler;

import cuit.epoch.pymjl.nettyidledemo.transport.NettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 19:01
 **/
public class ClientHandler extends AbstractCustomHeartbeatHandler {
    private NettyClient client;
    private int cnt = 0;

    public ClientHandler(NettyClient client) {
        super("client");
        this.client = client;
    }

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        byte[] data = new byte[byteBuf.readableBytes() - 5];
        byteBuf.skipBytes(5);
        byteBuf.readBytes(data);
        String content = new String(data);
        System.out.println(name + " get content: " + content);
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        cnt++;
        if (cnt > 5) {
            return;
        }
        super.handleAllIdle(ctx);
        sendPingMsg(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect();
    }
}
