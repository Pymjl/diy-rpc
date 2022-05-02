package cuit.epoch.pymjl.proxydemo.staticproxy.impl;

import cuit.epoch.pymjl.proxydemo.staticproxy.SmsService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 15:42
 **/
@Slf4j
public class ProxySmsServiceImpl implements SmsService {
    private final SmsService smsService;

    public ProxySmsServiceImpl(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void sendSms(String phone, String msg) {
        log.info("This is a proxy Object,Before calling the method to send SMS, we can do something else");
        log.info("Proxy call the method to Send SMS to [" + phone + " ] with msg [" + msg + "]");
        smsService.sendSms(phone, msg);
        log.info("After calling the method to send SMS, we can do something else");
    }

    public SmsService getSmsService() {
        return smsService;
    }
}
