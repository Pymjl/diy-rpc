package cuit.epoch.pymjl.proxydemo.dynamicproxy.cglib;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 16:25
 **/
@Slf4j
public class AliSmsService {
    public String sendMessage(String phone, String message) {
        log.info("This is a target Object: AliSmsService");
        log.info("AliSmsService start send Message to [" + phone + "]: [" + message + "]");
        return "AliSmsService send Message to [" + phone + "]: [" + message + "] Success";
    }
}
