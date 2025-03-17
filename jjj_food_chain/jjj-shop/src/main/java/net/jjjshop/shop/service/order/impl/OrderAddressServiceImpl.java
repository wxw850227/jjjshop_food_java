package net.jjjshop.shop.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.mapper.order.OrderAddressMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.param.order.OrderAddressParam;
import net.jjjshop.shop.service.order.OrderAddressService;
import net.jjjshop.shop.service.order.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单收货地址记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderAddressServiceImpl  extends BaseServiceImpl<OrderAddressMapper, OrderAddress> implements OrderAddressService {

    @Autowired
    private OrderService orderService;

    /**
     * 修改订单收货地址
     * @param
     * @return
     */
    public Boolean updateAddress(OrderAddressParam orderAddressParam){
        Order order = orderService.getById(orderAddressParam.getOrderId());
        if(order.getDeliveryType() == 10 && order.getDeliveryStatus()==20){
            throw new BusinessException("已发货订单不允许修改");
        }
        OrderAddress orderAddress = new OrderAddress();
        BeanUtils.copyProperties(orderAddressParam, orderAddress);
        return this.updateById(orderAddress);
    }

}
