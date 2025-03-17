package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderCancelParam对象", description = "订单取消参数")
public class OrderCancelParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("备注")
    private String cancelRemark;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;
}

