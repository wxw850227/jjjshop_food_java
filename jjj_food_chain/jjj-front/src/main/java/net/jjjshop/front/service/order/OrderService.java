package net.jjjshop.front.service.order;

import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.order.OrderCreateParam;
import net.jjjshop.front.param.order.OrderPageParam;
import net.jjjshop.front.param.order.OrderPayParam;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.order.OrderListVo;

import java.util.Map;

/**
 * 订单记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderService extends BaseService<Order> {
    /**
     * 创建订单
     * @param params
     * @return
     */
    Integer createOrder(OrderCreateParam params);

    /**
     * 用户订单信息
     * @param orderId
     * @param userId
     * @return
     */
    Order getUserOrderDetail(Integer orderId, Integer userId);

    /**
     * 获取支付参数
     * @param orderPayParam
     * @param user
     * @return
     */
    Map<String, Object> orderPay(OrderPayParam orderPayParam, User user);

    /**
     * 我的订单
     * @param orderPageParam
     * @return
     */
    Paging<OrderListVo> getList(OrderPageParam orderPageParam);

    /**
     * 用户取消订单
     * @param order
     * @return
     */
    Boolean cancelOrder(Order order);

    /**
     * 获取用户已购买商品数量
     * @param userId
     * @param productId
     * @return
     */
    Integer getHasBuyOrderNum(Integer userId, Integer productId);

    /**
     * 获取活动订单
     * 已付款，未取消
     * @param userId
     * @param productId
     * @return
     */
    Integer getPlusOrderNum(Integer userId, Integer productId, Integer orderSource);

    /**
     * 用户确认收货
     * @param orderId
     * @return
     */
    Boolean receipt(Integer orderId);

    /**
     * 根据类型查询订单数量
     * @param userId
     * @param type
     * @return
     */
    Integer getCount(Integer userId, String type);

    /**
     * 通过订单编号获取订单详情
     * @param orderNo
     * @return
     */
    OrderDetailVo detailByNo(String orderNo);

    /**
     * 通过订单Id获取订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Integer orderId);

    /**
     * 订单核销
     * @param orderId
     * @param extractClerkId
     * @return
     */
    Boolean verificationOrder(Integer orderId,Integer extractClerkId);
}
