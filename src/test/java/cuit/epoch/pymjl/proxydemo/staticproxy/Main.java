package cuit.epoch.pymjl.proxydemo.staticproxy;

import cuit.epoch.pymjl.proxydemo.staticproxy.impl.ProxySmsServiceImpl;
import cuit.epoch.pymjl.proxydemo.staticproxy.impl.SmsServiceImpl;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 15:46
 **/
public class Main {
    public static void main(String[] args) {
        //创建Sms服务对象
        SmsService smsService = new SmsServiceImpl();
        // 创建一个代理对象
        SmsService proxy = new ProxySmsServiceImpl(smsService);
        //调用代理对象的方法
        proxy.sendSms("18888888888", "Hello i am a proxy");

    }
}
