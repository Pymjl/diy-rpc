package cuit.epoch.pymjl.annotations;

import java.lang.annotation.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/26 19:34
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Inherited
public @interface MyAnnotations {
    String value() default "";

}
