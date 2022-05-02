package cuit.epoch.pymjl.annotations;

import java.lang.annotation.*;

/**
 * 表名的映射注解
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/29 23:57
 **/
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableAnnotation {
    /**
     * 表名
     */
    String value() default "";
}
