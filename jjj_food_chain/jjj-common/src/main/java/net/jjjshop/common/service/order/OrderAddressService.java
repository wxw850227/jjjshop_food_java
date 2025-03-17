package net.jjjshop.common.service.order;

import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 订单收货地址记录表 服务类
 *
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderAddressService extends BaseService<OrderAddress> {

//    /**
//     * 获取订单收货地址Vo
//     * @param orderId
//     * @return
//     */
//    OrderAddressVo getOrderAddress(Integer orderId);
    String getFullAddress(OrderAddress orderAddress);
}
