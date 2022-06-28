package cuit.epoch.pymjl.serialize.protostuff;

import cuit.epoch.pymjl.serialize.Serializer;
import cuit.epoch.pymjl.utils.ProtostuffUtils;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 21:35
 **/
public class ProtoStuffSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return ProtostuffUtils.serialize(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return ProtostuffUtils.deserialize(bytes, clazz);
    }
}
