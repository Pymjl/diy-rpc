package cuit.epoch.pymjl.remote.transport.netty.codec;

import cuit.epoch.pymjl.compress.Compress;
import cuit.epoch.pymjl.enums.CompressTypeEnum;
import cuit.epoch.pymjl.enums.SerializationTypeEnum;
import cuit.epoch.pymjl.remote.constants.RpcConstants;
import cuit.epoch.pymjl.remote.entity.RpcMessage;
import cuit.epoch.pymjl.serialize.Serializer;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 自定义协议
 * <p>
 * <pre>
 *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
 *   +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 *   |                                                                                                       |
 *   |                                         body                                                          |
 *   |                                                                                                       |
 *   |                                        ... ...                                                        |
 *   +-------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
 * body（object类型数据）
 * </pre>
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 23:09
 **/
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    /**
     * 编码
     *
     * @param ctx        上下文
     * @param rpcMessage 编码的对象
     * @param out        缓冲区
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf out) {
        try {
            //写入魔数
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            //写入版本
            out.writeByte(RpcConstants.VERSION);
            //先留4个字节占位，用于存储数据全长,writeIndex=5+4=9
            out.writerIndex(out.writerIndex() + 4);
            byte messageType = rpcMessage.getMessageType();
            //写入请求的类型
            out.writeByte(messageType);
            //写入对象的反序列化方式
            out.writeByte(rpcMessage.getCodec());
            //写入压缩的方式
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            //writeIndex=16
            // 将对象转为字节数组，然后得到信息的全长
            byte[] bodyBytes = null;
            //先将协议头数据加入其中
            int fullLength = RpcConstants.HEAD_LENGTH;
            //如果message Type不是心跳消息，full Length = head length + body length
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
                    && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                //序列化对象，先获取到序列化的方式
                String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
                log.info("codec name: [{}] ", codecName);
                //通过SPI获取序列化的对象
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                        .getExtension(codecName);
                //序列化
                bodyBytes = serializer.serialize(rpcMessage.getData());
                //对数据进行压缩
                String compressName = CompressTypeEnum.getName(rpcMessage.getCompress());
                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                        .getExtension(compressName);
                bodyBytes = compress.compress(bodyBytes);
                fullLength += bodyBytes.length;
            }
            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writeIndex = out.writerIndex();
            //将writeIndex重置到5，写入数据全长
            out.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            //写入数据全长后再重置到写入body后的索引
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
