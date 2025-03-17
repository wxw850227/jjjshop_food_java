

package net.jjjshop.front.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.product.Product;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("购物车商品详情VO")
public class ProductUserCartVo extends UserCart {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片")
    private String productImage;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品")
    private Product product;

    @ApiModelProperty("购物车商品总数量")
    private Integer cartTotalNum;

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

    @ApiModelProperty("是否开启会员折扣(1开启 0关闭)")
    private Integer isEnableGrade;

    @ApiModelProperty("是否开启积分赠送(1开启 0关闭)")
    private Integer isPointsGift;
}
