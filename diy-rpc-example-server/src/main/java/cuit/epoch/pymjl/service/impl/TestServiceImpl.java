package cuit.epoch.pymjl.service.impl;

import cuit.epoch.pymjl.annotations.RpcService;
import cuit.epoch.pymjl.service.TestService;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/11 22:34
 **/
@RpcService(version = "v1.0", group = "test")
public class TestServiceImpl implements TestService {
    @Override
    public String test(String msg) {
        return "服务端返回客户端调用的方法参数{" + msg + "}";

    }
}
