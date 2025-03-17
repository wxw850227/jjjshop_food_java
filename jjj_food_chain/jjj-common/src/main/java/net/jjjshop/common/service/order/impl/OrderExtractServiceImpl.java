package net.jjjshop.common.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderExtract;
import net.jjjshop.common.mapper.order.OrderExtractMapper;
import net.jjjshop.common.service.order.OrderExtractService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自提订单联系方式记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderExtractServiceImpl extends BaseServiceImpl<OrderExtractMapper, OrderExtract> implements OrderExtractService {

    @Autowired
    private OrderExtractMapper orderExtractMapper;

}
