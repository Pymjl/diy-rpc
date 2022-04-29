package cuit.epoch.pymjl.proxydemo.staticproxy.impl;

import cuit.epoch.pymjl.proxydemo.staticproxy.SmsService;
import lombok.extern.log4j.Log4j2;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 15:40
 **/
@Log4j2
public class SmsServiceImpl implements SmsService {
    @Override
    public void sendSms(String phone, String msg) {
        log.info("This is a target Object");
        log.info("Start send SMS：[{}]，to：[{}]", msg, phone);
    }
}
