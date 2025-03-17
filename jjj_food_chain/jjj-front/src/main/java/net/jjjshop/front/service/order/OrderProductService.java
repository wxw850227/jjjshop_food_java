package net.jjjshop.front.service.order;

import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.order.OrderProductVo;

import java.util.List;

/**
 * 订单商品记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderProductService extends BaseService<OrderProduct> {

    /**
     * 通过订单id查找商品
     * @param orderId
     * @return
     */
    List<OrderProduct> getProductList(Integer orderId);

    /**
     * 通过订单id查找商品vo
     * @param orderId
     * @return
     */
    List<OrderProductVo> getProductVoList(Integer orderId);
}
