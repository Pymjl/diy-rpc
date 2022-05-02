package cuit.epoch.pymjl.annotations.reflection;

import java.lang.reflect.Field;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/29 17:19
 **/
public class Test02 {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> c1 = Class.forName("cuit.epoch.pymjl.annotations.entity.Student");
        //获得包名+类名
        System.out.println(c1.getName());
        //获得类名
        System.out.println(c1.getSimpleName());
        //获得类的属性(public)
        Field[] fields = c1.getFields();
        //获得类的全部属性
        Field[] fields1 = c1.getDeclaredFields();
        for (int i = 0; i < fields1.length; i++) {
            System.out.println(fields1[i]);
        }
    }
}
