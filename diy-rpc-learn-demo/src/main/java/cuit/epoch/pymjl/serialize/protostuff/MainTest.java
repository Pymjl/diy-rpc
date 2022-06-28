package cuit.epoch.pymjl.serialize.protostuff;

import cuit.epoch.pymjl.serialize.entity.Student;
import cuit.epoch.pymjl.utils.ProtostuffUtils;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 21:06
 **/
public class MainTest {
    public static void main(String[] args) {
        Student student = new Student();
        student.setGender("男");
        student.setAge(20);
        student.setName("Pymjl");
        byte[] serialize = ProtostuffUtils.serialize(student);
        System.out.println(ProtostuffUtils.deserialize(serialize, Student.class));
    }
}
