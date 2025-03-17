package net.jjjshop.common.factory.sms;

import com.alibaba.fastjson.JSONObject;

/**
 * 支付成功工厂接口类
 */
public abstract class SmsFactoryService {

    /**
     * 发送短信
     */
    public abstract Boolean sendSms(String mobile, String templateCode, JSONObject templateParam,Long appId);
}
