package cuit.epoch.pymjl;

import cuit.epoch.pymjl.annotations.RpcReference;
import cuit.epoch.pymjl.service.TestService;
import org.springframework.stereotype.Component;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/11 22:36
 **/
@Component
public class TestController {

    @RpcReference(version = "v1.0", group = "test")
    private TestService testService;

    public void test(){
        System.out.println(testService.test("Client"));
    }

}
