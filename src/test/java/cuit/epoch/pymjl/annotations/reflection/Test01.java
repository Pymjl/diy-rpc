package cuit.epoch.pymjl.annotations.reflection;

import cuit.epoch.pymjl.annotations.entity.Student;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/29 16:17
 **/
public class Test01 {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("cuit.epoch.pymjl.annotations.entity.Student");
        System.out.println(clazz.hashCode());
        Class<Student> c2 = Student.class;
        System.out.println(c2.hashCode());
        System.out.println(System.getProperty("java.class.path"));
    }
}
