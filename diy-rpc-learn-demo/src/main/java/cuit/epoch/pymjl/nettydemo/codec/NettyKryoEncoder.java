package cuit.epoch.pymjl.nettydemo.codec;

import cuit.epoch.pymjl.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 22:03
 **/
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
    private final Class<?> clazz;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (clazz.isInstance(o)) {
            //将对象转换为byte
            byte[] bytes = KryoUtil.writeObjectToByteArray(o);
            //读取消息的长度
            int length = bytes.length;
            //将消息长度写入到字节数组的前4个字节
            byteBuf.writeInt(length);
            //将字节数组写入到缓冲区
            byteBuf.writeBytes(bytes);
        }

    }
}
