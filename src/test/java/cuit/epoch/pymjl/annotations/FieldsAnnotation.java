package cuit.epoch.pymjl.annotations;

import java.lang.annotation.*;

/**
 * ORM映射属性注解，将Java属性映射成表结构
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/26 19:34
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Inherited
public @interface FieldsAnnotation {
    String value() default "";

    String type() default "varchar";

    int length() default -1;

}
