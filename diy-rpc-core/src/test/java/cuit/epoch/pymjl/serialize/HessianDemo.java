package cuit.epoch.pymjl.serialize;

import cuit.epoch.pymjl.enums.SerializationTypeEnum;
import cuit.epoch.pymjl.remote.constants.RpcConstants;
import cuit.epoch.pymjl.remote.entity.RpcMessage;
import cuit.epoch.pymjl.serialize.hessian.HessianSerializer;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 19:10
 **/
public class HessianDemo {
    @Test
    void testMain() {
        Random random = new Random(100);
        Serializer serializer = new HessianSerializer();
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setRequestId(random.nextInt());
        rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
        rpcMessage.setData(random.nextInt());
        rpcMessage.setCodec(SerializationTypeEnum.HESSIAN.getCode());
        byte[] serialize = serializer.serialize(rpcMessage);
        System.out.println(serializer.deserialize(serialize, RpcMessage.class));
    }
}
