package net.jjjshop.shop.service.statistics.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.shop.mapper.statistics.ProductRankingMapper;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.param.statistics.RankingParam;
import net.jjjshop.shop.service.order.OrderProductService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.product.ProductService;
import net.jjjshop.shop.service.statistics.OrderRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单统计数据 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */

@Slf4j
@Service
public class OrderRankingServiceImpl implements OrderRankingService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRankingMapper productRankingMapper;


    /**
     * 获取订单统计数据
     * @param
     * @return
     */
    //获取订单统计数据
    public JSONObject getDataDetail(OrderPageParam param) {
        JSONObject json = productRankingMapper.getDataDetail(param);
        //营业总额 totalPrice,预计收入 incomeMoney,有效订单 orderCount,退款金额 refundMoney
        BigDecimal totalPrice = json.getBigDecimal("totalPrice");
        BigDecimal refundMoney = json.getBigDecimal("refundMoney");
        BigDecimal incomeMoney = totalPrice.subtract(refundMoney);
        json.put("incomeMoney",incomeMoney);
        return json;
    }

    /**
     * 获取商品统计数据
     * @param
     * @return
     */
    //获取商品统计数据
    public JSONObject getProductData() throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");

        Integer saleToday = productService.count(new LambdaQueryWrapper<Product>().eq(Product::getIsDelete, 0).eq(Product::getProductStatus, 10));
        String saleYesterday = "--";
        Integer noPayToday = orderProductService.getOrderProductData(today, null, "no_pay");
        Integer noPayYesterday = orderProductService.getOrderProductData(yesterday, null, "no_pay");
        Integer payToday = orderProductService.getOrderProductData(today, null, "pay");
        Integer payYesterday = orderProductService.getOrderProductData(yesterday, null, "pay");

        JSONObject json = new JSONObject();
        json.put("saleToday", saleToday);
        json.put("saleYesterday", saleYesterday);
        json.put("noPayToday", noPayToday);
        json.put("noPayYesterday", noPayYesterday);
        json.put("payToday", payToday);
        json.put("payYesterday", payYesterday);
        return json;
    }

    /**
     * 通过时间范围获取商品统计数据
     * @param
     * @return
     */
    //通过时间范围获取商品统计数据
    @Override
    public Map<String, Object> getOrderDataByDate(RankingParam rankingParam) {
        Map<String, Object> map = new HashMap<>();
        if(rankingParam.getStartDate() == null || rankingParam.getEndDate() == null){
            throw new BusinessException("搜索时间不能为空");
        }
        Date startTime = DateUtil.parse(rankingParam.getStartDate());
        Date endTime = DateUtil.parse(rankingParam.getEndDate());
        //endTime加一天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t,1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            Integer totalNum = 0;
            BigDecimal totalMoney = BigDecimal.ZERO;
            totalNum = Integer.parseInt(orderService.getOrderData(day, null, "order_total",0,-1).toString());
            if (totalNum != 0) {
                totalMoney = orderService.getOrderData(day, null, "order_total_price",0,-1);
            }
            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("totalNum", totalNum);
            json.put("totalMoney", totalMoney);
            json.put("saleMoney", totalMoney);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }

    /**
     * 获取用户已付款订单总数
     * @param startDate
     * @return
     */
    public Integer getPayOrderUserTotal(String startDate) throws ParseException {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Order::getPayTime, DateUtil.parse(startDate+" 00:00:00"));
        //如果结束查询时间为空,开始查询时间不为空，就默认设置时间查询区间为开始时间+1天
        wrapper.lt(Order::getPayTime,  DateUtil.parse(startDate+" 23:59:59"));
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.eq(Order::getPayStatus, 20);
        List<Order> orderList = orderService.list(wrapper);
        List<Integer> idList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
        return new HashSet<>(idList).size();
    }


}
