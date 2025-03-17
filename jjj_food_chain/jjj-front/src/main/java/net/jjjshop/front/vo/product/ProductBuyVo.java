

package net.jjjshop.front.vo.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.vo.product.ProductSkuVo;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("购买商品VO")
public class ProductBuyVo extends Product {
    private static final long serialVersionUID = 1L;

    private Integer cartId;

    @ApiModelProperty("会员id")
    private Integer userId;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("规格id")
    private Integer productSkuId;

    @ApiModelProperty("属性")
    private String attr;

    @ApiModelProperty("加料")
    private String feed;

    @ApiModelProperty("包装费")
    private BigDecimal bagPrice;

    @ApiModelProperty("总价")
    private BigDecimal price;

    @ApiModelProperty("商品价格")
    private BigDecimal productPrice;

    @ApiModelProperty("商品综合描述")
    private String describ;

    @ApiModelProperty("购物车类型0外卖1店内")
    private Integer cartType;

    @ApiModelProperty("购买商品sku")
    private ProductSku sku;

    @ApiModelProperty("购买数量")
    private Integer productNum;

    @ApiModelProperty("商品图片")
    private String productImage;

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

    @ApiModelProperty("赠送积分")
    private Integer pointsBonus;

    @ApiModelProperty("最大积分抵扣数量")
    private Integer maxPointsNum;

    @ApiModelProperty("积分抵扣数量")
    private BigDecimal pointsNum;

    @ApiModelProperty("积分抵扣金额")
    private BigDecimal pointsMoney;

    @ApiModelProperty("运费")
    private BigDecimal expressPrice;

    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponMoney;

    @ApiModelProperty("是否参与会员折扣")
    private Boolean isUserGrade;

    @ApiModelProperty("会员等级抵扣的金额")
    private BigDecimal gradeRatio;

    @ApiModelProperty("会员折扣的商品单价")
    private BigDecimal gradeProductPrice;

    @ApiModelProperty("会员折扣的总额差")
    private BigDecimal gradeTotalMoney;

    @ApiModelProperty("商城满减抵扣金额")
    private BigDecimal fullReduceMoney;

    @ApiModelProperty("商城满减抵扣金额")
    private BigDecimal productReduceMoney;

    @ApiModelProperty("拼团订单Id")
    private Integer billSourceId;

    @ApiModelProperty("活动id，对应拼团，秒杀，砍价活动id")
    private Integer activityId;

    @ApiModelProperty("订单商品来源id")
    private Integer productSourceId;

    @ApiModelProperty("订单商品sku来源id")
    private Integer skuSourceId;
}
