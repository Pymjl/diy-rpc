package cuit.epoch.pymjl.proxydemo.dynamicproxy.cglib;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 16:40
 **/
public class Main {
    public static void main(String[] args) {
        AliSmsService aliSmsService = (AliSmsService) CglibProxyFactory.getProxy(AliSmsService.class);
        aliSmsService.sendMessage("123456789", "hello");
    }
}
