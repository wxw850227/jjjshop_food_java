package net.jjjshop.job.service.order.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.mapper.order.OrderMapper;
import net.jjjshop.common.settings.vo.DeliverVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.message.MessageUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.job.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private MessageUtils messageUtils;

    /**
     * 通过交易号获取订单信息
     * @param tradeNo
     * @return
     */
    public Order detailByTradeNo(String tradeNo){
        return this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getTradeNo, tradeNo)
                .eq(Order::getIsDelete, 0));
    }

    @Override
    public boolean sellerDeliver(Order order) {
        // 获取商城配送设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.DELIVER.getKey(), Long.valueOf(order.getShopSupplierId()));
        DeliverVo deliverVo = JSONObject.toJavaObject(vo, DeliverVo.class);
        if("local".equals(deliverVo.getDefaults())){
            DeliverVo.Local local = JSON.parseObject(
                    JSON.toJSONString(deliverVo.getEngine().get("local")),
                    new TypeReference<DeliverVo.Local>() { });
            if(local.getTime() > 0){
                DateTime dateTime = DateUtil.offsetMinute(order.getPayTime(), local.getTime());
                if(new Date().after(dateTime)){
                    //配送
                    orderUtils.sendLocal(order);
                    // 发送发货通知
                    messageUtils.delivery(order);
                    return true;
                }
            }
        }
        return false;
    }
}
