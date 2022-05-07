package cuit.epoch.pymjl.annotations;

import java.lang.annotation.*;

/**
 * 标记接口，声明该接口是一个扩展点
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/3 12:05
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MySpi {
}
