package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderDeliver;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.vo.order.OrderProductVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("外卖配送详情VO")
public class OrderDeliverVo extends OrderDeliver {

    @ApiModelProperty("付款时间")
    private Date payTime;

    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    @ApiModelProperty("买家留言")
    private String buyerRemark;

    @ApiModelProperty("订单详情")
    private OrderVo orders;

    private Supplier supplier;

    @ApiModelProperty("配送公司(10商家配送20达达30配送员)")
    private String deliverSourceText;

    @ApiModelProperty("配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)")
    private String deliverStatusText;

    @ApiModelProperty("订单状态(10进行中 20被取消 30已完成)")
    private String statusText;

    @ApiModelProperty("实际付款金额(包含运费)")
    private BigDecimal payPrice;

}
