package net.jjjshop.common.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.mapper.order.OrderAddressMapper;
import net.jjjshop.common.service.order.OrderAddressService;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
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
     * @param orderAddress
     * @return
     */
    public String getFullAddress(OrderAddress orderAddress){
        if(orderAddress == null){
            return "";
        }
        return orderAddress.getDetail();
    }
}
