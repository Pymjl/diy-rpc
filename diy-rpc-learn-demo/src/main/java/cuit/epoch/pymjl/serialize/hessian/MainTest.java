package cuit.epoch.pymjl.serialize.hessian;

import com.caucho.hessian.io.HessianOutput;
import cuit.epoch.pymjl.serialize.entity.Student;

import java.io.ByteArrayOutputStream;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 18:59
 **/
public class MainTest {
    public static void main(String[] args) throws Exception {
        Student student = new Student("pymjl", "男", 20);
        byte[] serialize2 = Hessian2Utils.serialize(student);
        byte[] serialize = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            HessianOutput hessianOutput = new HessianOutput(bos);
            hessianOutput.writeObject(student);
            serialize = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("serialize failed");
        }
        System.out.println(serialize.length);
        System.out.println(serialize2.length);
        Student student1 = Hessian2Utils.deserialize(serialize2);
        System.out.println(student1);

    }
}
