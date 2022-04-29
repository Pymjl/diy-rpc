package cuit.epoch.pymjl.proxydemo.staticproxy;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/19 15:39
 **/
public interface SmsService {
    /**
     * 发送短信
     * @param phone 手机号
     * @param msg 短信内容
     */
    void sendSms(String phone, String msg);
}
