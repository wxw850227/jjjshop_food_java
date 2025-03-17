package net.jjjshop.shop.service.order;


import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.order.OrderAddressParam;

/**
 * 订单收货地址记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderAddressService extends BaseService<OrderAddress> {

    /**
     * 修改订单收货地址
     * @param
     * @return
     */
    Boolean updateAddress(OrderAddressParam orderAddressParam);
}
