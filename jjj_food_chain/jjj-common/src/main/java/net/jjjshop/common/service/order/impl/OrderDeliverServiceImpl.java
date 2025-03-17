package net.jjjshop.common.service.order.impl;

import net.jjjshop.common.entity.order.OrderDeliver;
import net.jjjshop.common.mapper.order.OrderDeliverMapper;
import net.jjjshop.common.service.order.OrderDeliverService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 外卖配送订单管理 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class OrderDeliverServiceImpl extends BaseServiceImpl<OrderDeliverMapper, OrderDeliver> implements OrderDeliverService {

    @Autowired
    private OrderDeliverMapper orderDeliverMapper;

}
