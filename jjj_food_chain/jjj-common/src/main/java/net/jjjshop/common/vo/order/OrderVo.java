package net.jjjshop.common.vo.order;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderProduct;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单VO")
public class OrderVo extends Order {

    private static final long serialVersionUID = 1L;

    private List<OrderProduct> product;
}
