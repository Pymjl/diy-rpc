package cuit.epoch.pymjl.serialize;

import cuit.epoch.pymjl.enums.CompressTypeEnum;
import cuit.epoch.pymjl.enums.SerializationTypeEnum;
import cuit.epoch.pymjl.remote.constants.RpcConstants;
import cuit.epoch.pymjl.remote.entity.RpcMessage;
import cuit.epoch.pymjl.serialize.protostuff.ProtoStuffSerializer;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 21:38
 **/
public class ProtoStuffTest {
    public static void main(String[] args) {
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
        rpcMessage.setRequestId(100);
        rpcMessage.setData(10001);
        rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
        rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
        Serializer serializer = new ProtoStuffSerializer();
        byte[] serialize = serializer.serialize(rpcMessage);
        System.out.println(serializer.deserialize(serialize, RpcMessage.class));

    }
}
