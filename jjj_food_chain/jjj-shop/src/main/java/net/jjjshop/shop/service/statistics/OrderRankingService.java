package net.jjjshop.shop.service.statistics;

import com.alibaba.fastjson.JSONObject;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.param.statistics.RankingParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

/**
 * 订单统计数据 服务类
 * @author jjjshop
 * @since 2022-06-28
 */

public interface OrderRankingService {

    /**
     * 获取商品统计数据
     * @param
     * @return
     */
    JSONObject getProductData() throws ParseException;

    /**
     * 通过时间范围获取商品统计数据
     * @param
     * @return
     */
    Map<String, Object> getOrderDataByDate(RankingParam rankingParam) ;

    /**
     * 获取付款用户总数
     * @param startDate
     * @return
     */
    Integer getPayOrderUserTotal(String startDate) throws ParseException;

    JSONObject getDataDetail(OrderPageParam param);
}
