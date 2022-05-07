package cuit.epoch.pymjl.annotations;

import java.lang.annotation.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/7 21:24
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RpcScan {
    /**
     * 扫描的包
     *
     * @return {@code String[]}
     */
    String[] basePackages();
}
