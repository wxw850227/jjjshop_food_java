package net.jjjshop.common.factory.paysuccess.source;

import net.jjjshop.common.enums.OrderSourceEnum;
import net.jjjshop.framework.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 支付成功工厂类
 */
@Component
public class PaySuccessSourceFactory {
    private static String packageName = "net.jjjshop.common.factory.paysuccess.source.impl.%sPaySuccessSourceFactoryService";
    @Autowired
    private Map<String, PaySuccessSourceFactoryService> factory;

    /**
     * 获取service
     * @param orderSource
     * @return
     */
    public PaySuccessSourceFactoryService getFactory(Integer orderSource){
        String className = String.format(packageName, OrderSourceEnum.getEName(orderSource));
        PaySuccessSourceFactoryService service = factory.get(className);
        if(service == null){
            throw new BusinessException("参数异常，未找到支付成功工厂类source");
        }
        return service;
    }
}
