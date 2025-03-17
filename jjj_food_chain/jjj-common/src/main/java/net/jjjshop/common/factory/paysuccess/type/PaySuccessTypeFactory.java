package net.jjjshop.common.factory.paysuccess.type;

import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.framework.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 支付成功工厂类
 */
@Component
public class PaySuccessTypeFactory {
    private static String packageName = "net.jjjshop.common.factory.paysuccess.type.impl.%sPaySuccessTypeFactoryService";
    @Autowired
    private Map<String, PaySuccessTypeFactoryService> factory;
    /**
     * 获取service
     * @param orderType
     * @return
     */
    public PaySuccessTypeFactoryService getFactory(Integer orderType){
        String className = String.format(packageName, OrderTypeEnum.getEName(orderType));
        PaySuccessTypeFactoryService service = factory.get(className);
        if(service == null){
            throw new BusinessException("参数异常，未找到支付成功工厂类type");
        }
        return service;
    }
}
