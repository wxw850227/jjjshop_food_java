package net.jjjshop.shop.service.statistics.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.param.home.HomeParam;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.param.statistics.RankingParam;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.statistics.HomeDataService;
import net.jjjshop.shop.service.statistics.OrderRankingService;
import net.jjjshop.shop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 首页统计数据 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */

@Slf4j
@Service
public class HomeDataServiceImpl implements HomeDataService {

    @Autowired
    private OrderRankingService orderRankingService;
    @Autowired
    private ProductRankingServiceImpl productRankingService;
    @Autowired
    private UserRankingServiceImpl userRankingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    /**
     * 获取首页显示数据
     * @param
     * @return
     */
    @Override
    public JSONObject getHomeData(HomeParam param) throws ParseException {
        JSONObject result = new JSONObject();
        //营业数据
        result.put("month_data", this.getMonthData(param.getShopSupplierId()));
        //订单概况
        result.put("orderData", this.getOrderData(param));
        //支付金额概况
        result.put("saleData", this.getSaleData(param.getSaleTime(),param.getShopSupplierId()));
        OrderPageParam orderPageParam = new OrderPageParam();
        orderPageParam.setShopSupplierId(param.getShopSupplierId());
        orderPageParam.setProductTime(param.getProductTime());
        orderPageParam.setProductSaleTime(param.getProductSaleTime());
        //销量排行榜
        result.put("salesNumRank", productRankingService.getSaleNumRank(orderPageParam));
        //销售额排行榜
        result.put("salesMoneyRank", productRankingService.getSaleMoneyRank(orderPageParam));
        result.put("top_data", this.getTopData(param));
        //数据更新时间
        result.put("update_time", net.jjjshop.framework.util.DateUtil.getDateString(new Date()));
        //待办事项
        result.put("wait_data", this.getWaitData(param));
        return result;
    }

    private JSONObject getWaitData(HomeParam param) {
        JSONObject result = new JSONObject();
        // --订单待办--
        JSONObject order = new JSONObject();
        //用餐方式0外卖1店内
        order.put("takeout", orderService.getReviewOrderTotal(0));
        order.put("store", orderService.getReviewOrderTotal(1));
        result.put("order", order);

        // --商品待办--
        JSONObject stock = new JSONObject();
        //库存告急 0外卖1店内
        stock.put("takeaway", productRankingService.getProductStockTotal(0));
        stock.put("store", productRankingService.getProductStockTotal(1));
        result.put("stock", stock);

        return result;
    }

    /**
     * 获取首页显示数据
     * @param
     * @return
     */
    private JSONObject getTopData(HomeParam param) {
        JSONObject result = new JSONObject();
        Date today = new Date();
        Date yesterday = DateUtil.offsetDay(today, -1);
        String t = net.jjjshop.framework.util.DateUtil.getDateString(today);
        String y = net.jjjshop.framework.util.DateUtil.getDateString(yesterday);
        Integer shopSupplierId = ShopLoginUtil.getShopSupplierId();
        // 用户总量
        result.put("user_total", userRankingService.getUserTotal());
        Integer user_today = userService.getUserData(t, null, "user_total");
        Integer user_yesterday = userService.getUserData(y, null, "user_total");
        BigDecimal user_rate = user_yesterday >0 ? BigDecimal.valueOf(user_today - user_yesterday)
                .divide(BigDecimal.valueOf(user_yesterday), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                BigDecimal.valueOf(user_today).multiply(BigDecimal.valueOf(100));
        result.put("user_today", user_today);
        result.put("user_yesterday", user_yesterday);
        result.put("user_rate", user_rate);
        // 订单总量
        result.put("order_total", orderService.getOrderData(null, null, "order_total",param.getShopSupplierId(),-1));
        Integer order_today = Integer.parseInt(orderService.getOrderData(t, null, "order_total",param.getShopSupplierId(),-1).toString());
        Integer order_yesterday = Integer.parseInt(orderService.getOrderData(y, null, "order_total",param.getShopSupplierId(),-1).toString());
        BigDecimal order_rate = order_yesterday >0 ? BigDecimal.valueOf(order_today - order_yesterday)
                .divide(BigDecimal.valueOf(order_yesterday), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                BigDecimal.valueOf(order_today).multiply(BigDecimal.valueOf(100));
        result.put("order_today", order_today);
        result.put("order_yesterday", order_yesterday);
        result.put("order_rate", order_rate);
        // 营业额
        result.put("total_money", orderService.getOrderData(null, null, "order_total_price",param.getShopSupplierId(),-1));
        BigDecimal total_money_today = orderService.getOrderData(t, null, "order_total_price",param.getShopSupplierId(),-1);
        BigDecimal total_money_yesterday = orderService.getOrderData(y, null, "order_total_price",param.getShopSupplierId(),-1);
        BigDecimal total_rate = total_money_yesterday.compareTo(BigDecimal.ZERO) > 0 ? total_money_today.subtract(total_money_yesterday)
                .divide(total_money_yesterday, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                total_money_today.multiply(BigDecimal.valueOf(100));
        result.put("total_money_today", total_money_today);
        result.put("total_money_yesterday", total_money_yesterday);
        result.put("total_rate", total_rate);
        //退款
        result.put("refund_money", orderService.getOrderData(null, null, "order_refund_money",param.getShopSupplierId(),-1));
        BigDecimal refund_money_today = orderService.getOrderData(t, null, "order_refund_money",param.getShopSupplierId(),-1);
        BigDecimal refund_money_yesterday = orderService.getOrderData(y, null, "order_refund_money",param.getShopSupplierId(),-1);
        BigDecimal refund_rate = refund_money_yesterday.compareTo(BigDecimal.ZERO) > 0 ? refund_money_today.subtract(refund_money_yesterday)
                .divide(refund_money_yesterday, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                refund_money_today.multiply(BigDecimal.valueOf(100));
        result.put("refund_money_today", refund_money_today);
        result.put("refund_money_yesterday", refund_money_yesterday);
        result.put("refund_rate", refund_rate);
        return result;
    }

    /**
     * 通过时间段查询订单数据
     */
    private Map<String, Object> getSaleData(Integer saleTime, Integer shopSupplierId) {
        RankingParam rankingParam = new RankingParam();
        Date end = new Date();
        Date start = this.getStartDate(saleTime,end);
        rankingParam.setStartDate(net.jjjshop.framework.util.DateUtil.getDateString(start));
        rankingParam.setEndDate(net.jjjshop.framework.util.DateUtil.getDateString(end));
        return orderRankingService.getOrderDataByDate(rankingParam);
    }

    private Date getStartDate(Integer type, Date end) {
        //默认今天
        Date start = end;
        //支付金额时间,1=本日,2=最近7天,3=最近15天,4=最近30天
        if(type != null && type == 2){
            start = DateUtil.offsetDay(end,-7);
        }
        if(type != null && type == 3){
            start = DateUtil.offsetDay(end,-15);
        }
        if(type != null && type == 4){
            start = DateUtil.offsetDay(end,-30);
        }
        return start;
    }

    private List<JSONObject> getOrderData(HomeParam param) {
        List<JSONObject> result = new ArrayList<>();
        Integer shopSupplierId = param.getShopSupplierId();
        //1=本日,2=最近7天,3=本月,4=本年
        Integer orderTime = param.getOrderTime();
        JSONObject result1 = new JSONObject();
        result1.put("name","外送订单");
        result1.put("value",this.getGeneralOrder(orderTime,shopSupplierId,10));
        result.add(result1);
        JSONObject result2 = new JSONObject();
        result2.put("name","自提订单");
        result2.put("value",this.getGeneralOrder(orderTime,shopSupplierId,20));
        result.add(result2);
        JSONObject result3 = new JSONObject();
        result3.put("name","堂食订单");
        result3.put("value",this.getGeneralOrder(orderTime,shopSupplierId,40));
        result.add(result3);
        JSONObject result4 = new JSONObject();
        result4.put("name","打包订单");
        result4.put("value",this.getGeneralOrder(param.getOrderTime(),param.getShopSupplierId(),30));
        result.add(result4);
        return result;
    }

    private Integer getGeneralOrder(Integer orderTime, Integer shopSupplierId, Integer deliveryType) {
        Date now = new Date();
        String startDate = DateUtil.format(now, "yyyy-MM-dd 00:00:00");
        String endDate =  DateUtil.format(now, "yyyy-MM-dd 23:59:59");
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        //门店ID
        if(shopSupplierId != null && shopSupplierId > 0){
            wrapper.eq(Order::getShopSupplierId, shopSupplierId);
        }
        //1=本日,2=最近7天,3=本月,4=本年
        if(orderTime != null){
            switch (orderTime){
                case 2:
                    startDate = DateUtil.format(DateUtil.offsetDay(now, -7), "yyyy-MM-dd 00:00:00");
                    break;
                case 3:
                    startDate = DateUtil.format(DateUtil.beginOfMonth(now), "yyyy-MM-dd 00:00:00");
                    break;
                case 4:
                    startDate = DateUtil.format(DateUtil.beginOfYear(now), "yyyy-MM-dd 00:00:00");
                    break;
                default:
                    break;
            }
        }
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐)
        wrapper.eq(Order::getDeliveryType, deliveryType);
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.eq(Order::getPayStatus, 20);
        wrapper.ne(Order::getOrderStatus, 20);
        wrapper.between(Order::getCreateTime, startDate,endDate);
        return orderService.count(wrapper);
    }

    private JSONObject getMonthData(Integer shopSupplierId) {
        JSONObject result = new JSONObject();
        Date now = new Date();
        //获取当月时间
        String month = DateUtil.format(now, "yyyy-MM-01");
        String monthEnd = DateUtil.format(now, "yyyy-MM-dd");
        //获取上月时间
        String lastMonth = DateUtil.format(DateUtil.offsetMonth(now, -1), "yyyy-MM-01");
        String lastMonthEnd = net.jjjshop.framework.util.DateUtil.getLastEndDate(1);
        //当月新增用户
        result.put("user_total", userService.getUserData(month, monthEnd, "user_total"));
        //上月新增用户
        result.put("user_total_last", userService.getUserData(lastMonth, lastMonthEnd, "user_total"));
        //当月营业额
        result.put("total_money", orderService.getOrderData(month, monthEnd, "order_total_price",shopSupplierId,-1));
        //上月营业额
        result.put("total_money_last", orderService.getOrderData(lastMonth, lastMonthEnd, "order_total_price",shopSupplierId,-1));
        //当月订单数
        result.put("order_total", orderService.getOrderData(month, monthEnd, "order_total",shopSupplierId,-1).intValue());
        //上月订单数
        result.put("order_total_last", orderService.getOrderData(lastMonth, lastMonthEnd, "order_total",shopSupplierId,-1).intValue());
        //当月退款
        result.put("refund_money", orderService.getOrderData(month, monthEnd, "order_refund_money",shopSupplierId,-1));
        //上月退款
        result.put("refund_money_last", orderService.getOrderData(lastMonth, lastMonthEnd, "order_refund_money",shopSupplierId,-1));
        return result;
    }
}
