package cuit.epoch.pymjl.remote.transport.netty.codec;

import cuit.epoch.pymjl.compress.Compress;
import cuit.epoch.pymjl.enums.CompressTypeEnum;
import cuit.epoch.pymjl.enums.SerializationTypeEnum;
import cuit.epoch.pymjl.remote.constants.RpcConstants;
import cuit.epoch.pymjl.remote.entity.RpcMessage;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.remote.entity.RpcResponse;
import cuit.epoch.pymjl.serialize.Serializer;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 自定义协议
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
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 22:01
 **/
@Slf4j
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 有参构造，采用Netty自带的LengthFieldBasedFrameDecoder，用于解决TCP粘包、拆包的问题
     * 因为需要手动校验魔数，版本等，所以并未对数据进行截取
     *
     * @param maxFrameLength    发送的数据包最大长度
     * @param lengthFieldOffset 长度字段偏移量，指的是长度域位于整个数据包字节数组中的下标
     * @param lengthFieldLength 长度域的自己的字节数长度
     */
    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public MessageDecoder() {
        //maxFrameLength：数据包最大大小 8M
        // lengthFieldOffset：将magic code和version作为head，它们共5字节，所以长度域偏移量为5
        // lengthFieldLength: 长度域的长度是一个整型来存放，占4个字节，所以为4
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            //对解码结果进行强转
            ByteBuf frame = (ByteBuf) decoded;
            //当缓冲区内的字节数大于头中的数据时再做拆分
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }

        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        // 必须按照协议的顺序来读对应的数据
        //先检查魔数
        checkMagicNumber(in);
        //校验版本
        checkVersion(in);
        //数据全长
        int fullLength = in.readInt();
        //构建 RpcMessage 对象
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .codec(codecType)
                .requestId(requestId)
                .messageType(messageType).build();
        //如果是心跳请求，则直接处理
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        //通过数据全长减去header的长度，得到request的自己数组长度
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] bs = new byte[bodyLength];
            in.readBytes(bs);
            //先对该数据进行解压，获取解压方式的名称
            String compressName = CompressTypeEnum.getName(compressType);
            //通过SPI机制选择解压方式
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                    .getExtension(compressName);
            //解压
            bs = compress.decompress(bs);
            //获取反序列化的方式
            String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
            log.info("codec name: [{}] ", codecName);
            //通过SPI机制获取加载对应的反序列实现类
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                    .getExtension(codecName);
            //如果是RPC请求，进行反序列化
            if (messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest tmpValue = serializer.deserialize(bs, RpcRequest.class);
                rpcMessage.setData(tmpValue);
            } else {
                RpcResponse tmpValue = serializer.deserialize(bs, RpcResponse.class);
                rpcMessage.setData(tmpValue);
            }
        }
        return rpcMessage;

    }

    private void checkVersion(ByteBuf in) {
        // read the version and compare
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("version isn't compatible" + version);
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        // 先读取位于包中最前面的四个字节，再进行比较
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
    }
}
