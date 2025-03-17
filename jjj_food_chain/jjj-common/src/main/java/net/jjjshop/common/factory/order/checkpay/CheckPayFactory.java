package net.jjjshop.common.factory.order.checkpay;

import net.jjjshop.common.enums.OrderSourceEnum;
import net.jjjshop.framework.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 商品工厂类
 */
@Component
public class CheckPayFactory {
    private static String packageName = "net.jjjshop.common.factory.order.checkpay.impl.%sCheckPayFactoryService";
    @Autowired
    private Map<String, CheckPayFactoryService> factory;
    /**
     * 获取service
     * @param orderSource
     * @return
     */
    public CheckPayFactoryService getFactory(Integer orderSource){
        String className = String.format(packageName, OrderSourceEnum.getEName(orderSource));
        CheckPayFactoryService service = factory.get(className);
        if(service == null){
            throw new BusinessException("参数异常，未找到支付检查工厂类");
        }
        return service;
    }
}
