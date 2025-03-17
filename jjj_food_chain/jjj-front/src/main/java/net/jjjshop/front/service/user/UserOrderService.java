package net.jjjshop.front.service.user;


import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.settings.ExpressDetailVo;

/**
 * 用户订单 实现类
 * @author jjjshop
 * @since 2022-08-02
 */

public interface UserOrderService {

    /**
     * 获取订单详情
     * @param user
     * @param orderId
     * @return
     */
    OrderDetailVo detail(User user, Integer orderId);


    /**
     * 转换订单详情VO
     * @param order
     * @param
     */
    OrderDetailVo transOrderDetailVo(Order order);
}
