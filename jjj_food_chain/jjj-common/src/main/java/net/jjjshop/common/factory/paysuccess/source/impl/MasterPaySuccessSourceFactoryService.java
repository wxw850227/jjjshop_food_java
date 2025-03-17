package net.jjjshop.common.factory.paysuccess.source.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.paysuccess.source.PaySuccessSourceFactoryService;
import net.jjjshop.common.factory.printer.PrinterFactory;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.SettingUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付成功
 */
@Slf4j
@Service
public class MasterPaySuccessSourceFactoryService extends PaySuccessSourceFactoryService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 支付成功处理
     */
    @Async
    public void onPaySuccess(Integer orderId){
        Order order = orderService.getById(orderId);
        // 公共事件
        this.onCommonEvent(order);
    }
}
