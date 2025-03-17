package net.jjjshop.common.factory.sms;

import net.jjjshop.framework.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 支付成功工厂类
 */
@Component
public class SmsFactory {
    private static String packageName = "net.jjjshop.common.factory.sms.impl.%sSmsFactoryService";
    @Autowired
    private Map<String, SmsFactoryService> factory;

    /**
     * 获取service
     * @param
     * @return
     */
    public SmsFactoryService getFactory(String smsType){
        String className = String.format(packageName, smsType);
        SmsFactoryService service = factory.get(className);
        if(service == null){
            throw new BusinessException("参数异常，未找到短信工厂类");
        }
        return service;
    }
}
