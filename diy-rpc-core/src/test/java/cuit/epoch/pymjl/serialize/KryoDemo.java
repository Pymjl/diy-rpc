package cuit.epoch.pymjl.serialize;

import cuit.epoch.pymjl.remote.entity.RpcMessage;
import cuit.epoch.pymjl.remote.entity.RpcRequest;
import cuit.epoch.pymjl.serialize.kyro.KryoSerializer;
import cuit.epoch.pymjl.spi.ExtensionLoader;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/23 19:59
 **/
public class KryoDemo {
    @Test
    void testSerialize() {
        RpcRequest request = new RpcRequest();
        request.setVersion("version 1.0");
        request.setRequestId(UUID.randomUUID().toString().replaceAll("-", ""));
        request.setInterfaceName("test");
        request.setGroup("group1");
        Serializer serializer = new KryoSerializer();
        byte[] serialize = serializer.serialize(request);

        System.out.println(serializer.deserialize(serialize, RpcRequest.class));
    }

    @Test
    void test1() {
        System.out.println(Serializer.class.getCanonicalName());
        System.out.println(KryoSerializer.class.getCanonicalName());
        String s = "你好";
        Serializer serializer = new KryoSerializer();
        byte[] bytes = serializer.serialize(s);
        for (byte aByte : bytes) {
            System.out.print(Integer.toString(aByte, 16));
        }
    }

    @Test
    void testSPI() {
        Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                .getExtension("kryo");
        RpcMessage message = new RpcMessage();
        message.setMessageType((byte) 1);
        message.setCodec((byte) 2);
        message.setData(new String("hello world"));
        message.setRequestId(10);
        byte[] bytes = serializer.serialize(message);
        System.out.println(serializer.deserialize(bytes, RpcMessage.class));
    }
}
