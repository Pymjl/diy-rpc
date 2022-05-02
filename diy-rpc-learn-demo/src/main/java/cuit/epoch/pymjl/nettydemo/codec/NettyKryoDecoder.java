package cuit.epoch.pymjl.nettydemo.codec;

import cuit.epoch.pymjl.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/18 22:02
 **/
@AllArgsConstructor
@Slf4j
public class NettyKryoDecoder extends ByteToMessageDecoder {
    private final Class<?> clazz;
    /**
     * Netty传输的数据长度在前四个字节
     */
    private static final int BODY_LENGTH = 4;

    /**
     * 解码
     *
     * @param ctx 上下文
     * @param in  字节缓冲
     * @param out 存放解码后的对象
     * @throws Exception 异常
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //消息的长度必须大于四
        if (in.readableBytes() >= BODY_LENGTH) {
            //标记当前的readIndex的位置，方便后面重置
            in.markReaderIndex();
            //读取数据长度
            int dataLength = in.readInt();
            //判断数据是否齐全
            if (in.readableBytes() >= dataLength) {
                //读取完整的数据
                byte[] data = new byte[dataLength];
                in.readBytes(data);
                //解码
                Object obj = KryoUtil.readObjectFromByteArray(data, clazz);
                out.add(obj);
                log.info("解码成功：" + obj);
            }
        }

    }
}
