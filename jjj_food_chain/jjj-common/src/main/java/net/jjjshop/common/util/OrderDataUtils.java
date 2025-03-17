package net.jjjshop.common.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.service.order.OrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderDataUtils {

    @Autowired
    private OrderService orderService;


    /**
     * 获取订单统计数据
     *
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    //获取订单统计数据
    public BigDecimal getOrderData(String startDate, String endDate, String type, Integer shopSupplierId) throws ParseException {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        //开始查询时间不为空
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(Order::getPayTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        //结束查询时间不为空
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(Order::getPayTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            //如果结束查询时间为空,开始查询时间不为空，就默认设置时间查询区间为开始时间+1天
            Date date = DateUtil.parse(startDate + " 23:59:59");
            wrapper.le(Order::getPayTime, date);
        }
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.eq(Order::getPayStatus, 20);
        wrapper.ne(Order::getOrderStatus, 20);
        if (shopSupplierId != null && shopSupplierId > 0) {
            wrapper.eq(Order::getShopSupplierId, shopSupplierId);
        }
        //根据查询模式返回不同的数值
        if ("order_total".equals(type)) {
            //查询订单总数
            return new BigDecimal(orderService.count(wrapper)).setScale(0, RoundingMode.DOWN);
        } else if ("order_total_price".equals(type)) {
            //查询付款订单总额
            List<Order> list = orderService.list(wrapper);
            BigDecimal result = BigDecimal.ZERO;
            for (Order o : list) {
                result = result.add(o.getPayPrice());
            }
            return result;
        } else if ("order_user_total".equals(type)) {
            //查询付款用户数
            List<Order> orderList = orderService.list(wrapper);
            List<Integer> idList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
            return new BigDecimal(new HashSet<>(idList).size()).setScale(0, BigDecimal.ROUND_DOWN);
        }
        return BigDecimal.ZERO.setScale(0, BigDecimal.ROUND_DOWN);
    }

    /**
     * 获取订单数据
     */
    public JSONObject getData(Integer shopSupplierId) throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");
        // 销售额(元)
        BigDecimal orderTotalPriceT = this.getOrderData(today, null, "order_total_price", shopSupplierId);
        BigDecimal orderTotalPriceY = this.getOrderData(yesterday, null, "order_total_price", shopSupplierId);
        // 支付订单数
        Integer orderTotalT = Integer.parseInt(this.getOrderData(today, null, "order_total", shopSupplierId).toString());
        Integer orderTotalY = Integer.parseInt(this.getOrderData(yesterday, null, "order_total", shopSupplierId).toString());
        //下单用户数
        Integer orderUserTotalT = Integer.parseInt(this.getOrderData(today, null, "order_user_total", shopSupplierId).toString());
        Integer orderUserTotalY = Integer.parseInt(this.getOrderData(yesterday, null, "order_user_total", shopSupplierId).toString());
        // 客单价
        BigDecimal orderPerPriceT = BigDecimal.ZERO;
        BigDecimal orderPerPriceY = BigDecimal.ZERO;
        if (orderUserTotalT > 0) {
            orderPerPriceT = orderTotalPriceT.divide(new BigDecimal(orderUserTotalT), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
        }
        if (orderUserTotalY > 0) {
            orderPerPriceY = orderTotalPriceY.divide(new BigDecimal(orderUserTotalY),RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
        }
        JSONObject result = new JSONObject();
        result.put("orderTotalPriceT", orderTotalPriceT);
        result.put("orderTotalPriceY", orderTotalPriceY);

        result.put("orderTotalT", orderTotalT);
        result.put("orderTotalY", orderTotalY);

        result.put("orderUserTotalT", orderUserTotalT);
        result.put("orderUserTotalY", orderUserTotalY);

        result.put("orderPerPriceT", orderPerPriceT);
        result.put("orderPerPriceY", orderPerPriceY);

        return result;
    }
}
