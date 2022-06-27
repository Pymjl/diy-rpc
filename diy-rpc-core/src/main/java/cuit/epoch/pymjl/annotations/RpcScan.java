package cuit.epoch.pymjl.annotations;

import cuit.epoch.pymjl.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/7 21:24
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Documented
public @interface RpcScan {
    /**
     * 扫描的包
     *
     * @return {@code String[]}
     */
    String[] basePackage();
}
