package cuit.epoch.pymjl.annotations;

import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 通过反射获取注解
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/29 23:49
 **/
@Log4j2
public class AnnotationsTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        Class<?> clazz = Class.forName("cuit.epoch.pymjl.annotations.entity.Student");
        //通过反射获取注解,这是只能获取类上面的注解
        log.info("获取类的注解");
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        //获取注解的属性值
        log.info("获取类的属性值");
        TableAnnotation tableAnnotation = (TableAnnotation) clazz.getAnnotation(TableAnnotation.class);
        System.out.println("value="+tableAnnotation.value());
        //获取属性的注解
        //先获取对应的属性
        Field name = clazz.getDeclaredField("name");
        FieldsAnnotation nameAnnotation = name.getAnnotation(FieldsAnnotation.class);
        System.out.println(nameAnnotation.value());
        System.out.println(nameAnnotation.length());
        System.out.println(nameAnnotation.type());

    }
}
