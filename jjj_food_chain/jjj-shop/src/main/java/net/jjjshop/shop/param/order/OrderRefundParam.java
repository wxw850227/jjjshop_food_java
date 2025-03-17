package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderRefundParam对象", description = "售后单参数")
public class OrderRefundParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("审核状态")
    private Integer isAgree;

    @ApiModelProperty("orderRefundId")
    private Integer orderRefundId;

    @ApiModelProperty("拒绝原因")
    private String refuseDesc;

    @ApiModelProperty("售后发货地址")
    private Integer addressId;

    @ApiModelProperty("售后单换货物流Id")
    private Integer sendExpressId;

    @ApiModelProperty("售后单换货物流编号")
    private String sendExpressNo;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

}
