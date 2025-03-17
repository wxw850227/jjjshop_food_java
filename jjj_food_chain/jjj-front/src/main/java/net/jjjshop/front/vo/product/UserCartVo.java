

package net.jjjshop.front.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.UserCart;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("购物车详情VO")
public class UserCartVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购物车商品总数量")
    private Integer cartTotalNum;

    @ApiModelProperty("最低消费差额")
    private BigDecimal minMoneyDiff;

    @ApiModelProperty("包装费")
    private BigDecimal totalBagPrice;

    @ApiModelProperty("划线金额总价")
    private BigDecimal totalLineMoney;

    @ApiModelProperty("实际支付金额")
    private BigDecimal totalPayPrice;

    @ApiModelProperty("购物车总价")
    private BigDecimal totalPrice;

    @ApiModelProperty("商品总价")
    private BigDecimal totalProductPrice;
}
