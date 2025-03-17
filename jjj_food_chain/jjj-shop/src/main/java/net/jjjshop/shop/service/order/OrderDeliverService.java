package net.jjjshop.shop.service.order;

import net.jjjshop.common.entity.order.OrderDeliver;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.vo.order.OrderDeliverVo;
import net.jjjshop.shop.vo.order.OrderVo;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 外卖配送订单管理 服务类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
public interface OrderDeliverService extends BaseService<OrderDeliver> {

    Paging<OrderDeliverVo> getList(OrderPageParam orderPageParam);

    Map<String,Object> detail(Integer deliveId);

    boolean verify(Integer orderId);

    boolean cancel(Integer deliveId);

    void exportList(OrderPageParam orderPageParam, HttpServletResponse httpServletResponse);
}
