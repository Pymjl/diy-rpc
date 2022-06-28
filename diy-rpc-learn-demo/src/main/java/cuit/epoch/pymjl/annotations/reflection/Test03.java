package cuit.epoch.pymjl.annotations.reflection;

import cuit.epoch.pymjl.annotations.reflection.service.impl.AllServiceImpl;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 17:40
 **/
public class Test03 {
    public static void main(String[] args) {
        Class<?> clazz = AllServiceImpl.class;
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> c : interfaces) {
            System.out.println(c.getCanonicalName());
        }
    }
}
