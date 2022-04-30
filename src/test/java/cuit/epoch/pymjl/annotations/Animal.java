package cuit.epoch.pymjl.annotations;

import java.lang.annotation.Repeatable;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/30 15:09
 **/
@Repeatable(Animals.class)
public @interface Animal {
    String type() default "";
}
