package net.jjjshop.common.factory.product;

import net.jjjshop.common.enums.OrderSourceEnum;
import net.jjjshop.framework.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 商品工厂类
 */
@Component
public class ProductFactory {
    private static String packageName = "net.jjjshop.common.factory.product.impl.%sProductFactoryService";
    @Autowired
    private Map<String, ProductFactoryService> factory;
    /**
     * 获取service
     * @param orderSource
     * @return
     */
    public ProductFactoryService getFactory(Integer orderSource){
        String className = String.format(packageName, OrderSourceEnum.getEName(orderSource));
        ProductFactoryService service = factory.get(className);
        if(service == null){
            throw new BusinessException("参数异常，未找到商品工厂类");
        }
        return service;
    }
}
