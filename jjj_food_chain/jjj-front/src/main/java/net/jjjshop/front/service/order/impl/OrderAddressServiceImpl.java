package net.jjjshop.front.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.mapper.order.OrderAddressMapper;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.order.OrderAddressService;
import net.jjjshop.front.vo.order.OrderAddressVo;
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
public class OrderAddressServiceImpl extends BaseServiceImpl<OrderAddressMapper, OrderAddress> implements OrderAddressService {

    @Autowired
    private RegionService regionService;

    /**
     * 获取订单收货地址Vo
     * @param orderId
     * @return
     */
    public OrderAddressVo getOrderAddress(Integer orderId) {
        OrderAddress orderAddress = this.getOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, orderId));
        if (orderAddress != null) {
            OrderAddressVo orderAddressVo = new OrderAddressVo();
            BeanUtils.copyProperties(orderAddress, orderAddressVo);
            return orderAddressVo;
        } else {
            return null;
        }

    }
}
