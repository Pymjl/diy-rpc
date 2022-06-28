package cuit.epoch.pymjl.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 18:54
 **/
public class Hessian2Utils {
    /**
     * JavaBean序列化.
     *
     * @param javaBean Java对象.
     * @throws Exception 异常信息.
     */
    public static <T> byte[] serialize(T javaBean) throws Exception {
        Hessian2Output ho = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ho = new Hessian2Output(baos);
            ho.writeObject(javaBean);
            ho.flush();
            return baos.toByteArray();
        } catch (Exception ex) {
            System.out.println("[模拟日志记录]HessianUtils.serialize.异常." + ex.getMessage());
            throw new Exception("HessianUtils.serialize.异常.", ex);
        } finally {
            if (null != ho) {
                ho.close();
            }
        }
    }

    /**
     * JavaBean反序列化.
     *
     * @param serializeData 序列化数据.
     * @throws Exception 异常信息.
     */
    public static <T> T deserialize(byte[] serializeData) throws Exception {
        T javaBean = null;
        Hessian2Input hi = null;
        ByteArrayInputStream bais = null;

        try {
            bais = new ByteArrayInputStream(serializeData);
            hi = new Hessian2Input(bais);
            javaBean = (T) hi.readObject();
            return javaBean;
        } catch (Exception ex) {
            System.out.println("[模拟日志记录]HessianUtils.deserialize.异常." + ex.getMessage());
            throw new Exception("HessianUtils.deserialize.异常.", ex);
        } finally {
            if (null != hi) {
                hi.close();
            }
        }
    }
}
