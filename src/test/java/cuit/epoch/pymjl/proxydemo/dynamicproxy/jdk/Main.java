package cuit.epoch.pymjl.proxydemo.dynamicproxy.jdk;

import cuit.epoch.pymjl.proxydemo.staticproxy.SmsService;
import cuit.epoch.pymjl.proxydemo.staticproxy.impl.SmsServiceImpl;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 16:15
 **/
public class Main {
    public static void main(String[] args) {
        SmsService smsService = (SmsService) JdkProxyFactory.getProxy(new SmsServiceImpl());
        smsService.sendSms("12345678901", "hello");
    }
}
