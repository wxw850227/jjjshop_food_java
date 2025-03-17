package net.jjjshop.shop.service.order;

import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.framework.common.service.BaseService;

import java.text.ParseException;

/**
 * 订单商品记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */

public interface OrderProductService extends BaseService<OrderProduct> {

    /**
     * 按照时间范围获取商品订单数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    int getOrderProductData(String startDate, String endDate, String type) throws ParseException;
}
