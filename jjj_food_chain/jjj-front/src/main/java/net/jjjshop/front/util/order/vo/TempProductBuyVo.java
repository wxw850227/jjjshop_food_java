package net.jjjshop.front.util.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 临时商品购买vo，用于计算
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("临时商品购买vo")
public class TempProductBuyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品id")
    private Integer productId;

    @ApiModelProperty("商品总价")
    private BigDecimal totalPrice;

    @ApiModelProperty("商品总价,单位分")
    private BigDecimal totalPriceFen;

    @ApiModelProperty("权重")
    private BigDecimal weight;

    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponMoney;

    @ApiModelProperty("商城满减抵扣金额")
    private BigDecimal fullReduceMoney;
}
