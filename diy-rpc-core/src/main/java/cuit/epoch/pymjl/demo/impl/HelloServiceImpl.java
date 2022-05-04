package cuit.epoch.pymjl.demo.impl;

import cuit.epoch.pymjl.service.HelloService;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/4 12:17
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("客户端你好，我是服务端");
    }
}
