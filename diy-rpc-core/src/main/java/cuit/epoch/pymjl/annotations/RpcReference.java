package cuit.epoch.pymjl.annotations;

import java.lang.annotation.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/8 0:13
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface RpcReference {
    /**
     * 版本
     *
     * @return {@code String}
     */
    String version() default "";

    /**
     * 分组
     *
     * @return {@code String}
     */
    String group() default "";
}
