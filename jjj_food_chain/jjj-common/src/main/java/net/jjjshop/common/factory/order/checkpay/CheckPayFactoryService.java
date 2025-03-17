package net.jjjshop.common.factory.order.checkpay;

import net.jjjshop.common.entity.order.OrderProduct;

import java.util.List;

/**
 * 商品工厂接口类
 */
public interface CheckPayFactoryService {
    /**
     * 判断订单是否允许付款
     */
    void checkOrderStatus(List<OrderProduct> productList);
}
