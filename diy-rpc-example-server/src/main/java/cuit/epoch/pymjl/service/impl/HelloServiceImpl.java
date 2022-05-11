package cuit.epoch.pymjl.service.impl;

import cuit.epoch.pymjl.service.HelloService;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/11 21:58
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String msg) {
        System.out.println("来自客户端的消息" + msg);
    }
}
