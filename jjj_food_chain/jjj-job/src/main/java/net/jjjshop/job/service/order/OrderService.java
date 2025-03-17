package net.jjjshop.job.service.order;

import net.jjjshop.common.entity.order.Order;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 订单记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderService extends BaseService<Order> {
    /**
     * 通过交易号获取订单信息
     * @param tradeNo
     * @return
     */
    Order detailByTradeNo(String tradeNo);

    boolean sellerDeliver(Order order);
}
