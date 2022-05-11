package cuit.epoch.pymjl.annotations;

import java.lang.annotation.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/8 0:08
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RpcService {
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
