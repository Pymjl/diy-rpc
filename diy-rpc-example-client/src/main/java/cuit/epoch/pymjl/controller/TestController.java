package cuit.epoch.pymjl.controller;

import cuit.epoch.pymjl.annotations.RpcReference;
import cuit.epoch.pymjl.service.TestService;
import org.springframework.stereotype.Component;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/27 12:40
 **/
@Component
public class TestController {
    @RpcReference(version = "v1.0", group = "test")
    private TestService testService;

    public void test() {
        String test = testService.test("Client");
        System.out.println(test);
    }
}
