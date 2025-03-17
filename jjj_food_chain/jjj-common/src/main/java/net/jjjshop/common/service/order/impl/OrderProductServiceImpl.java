package net.jjjshop.common.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.mapper.order.OrderProductMapper;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderProductServiceImpl extends BaseServiceImpl<OrderProductMapper, OrderProduct> implements OrderProductService {

}
