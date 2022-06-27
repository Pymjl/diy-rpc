package cuit.epoch.pymjl;

import cuit.epoch.pymjl.annotations.RpcScan;
import cuit.epoch.pymjl.controller.TestController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/27 12:31
 **/
@RpcScan(basePackage = "cuit.epoch.pymjl")
public class NettyClientMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        TestController testController = (TestController) applicationContext.getBean("testController");
        testController.test();
    }
}
