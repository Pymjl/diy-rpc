package cuit.epoch.pymjl.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import cuit.epoch.pymjl.exception.SerializeException;
import cuit.epoch.pymjl.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 19:01
 **/
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        Hessian2Output ho = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ho = new Hessian2Output(baos);
            ho.writeObject(obj);
            ho.flush();
            return baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SerializeException("serialize failed");
        } finally {
            if (null != ho) {
                try {
                    ho.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Hessian2Input hi = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            hi = new Hessian2Input(bais);
            Object o = hi.readObject();
            return clazz.cast(o);
        } catch (Exception ex) {
            throw new SerializeException("deserialize failed");
        } finally {
            if (null != hi) {
                try {
                    hi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bais) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
