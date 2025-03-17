

package net.jjjshop.front.param.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 微信小程序登录参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CartParam", description = "购物车参数对象")
public class CartParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("会员id")
    private Integer userId;

    @ApiModelProperty("配送方式(10外卖配送 20上门取30打包带走40店内就餐")
    @NotNull(message = "配送方式不能为空")
    private Integer delivery;

    @ApiModelProperty("商品id")
    @NotNull(message = "商品id不能为空")
    private Integer productId;

    @ApiModelProperty("商品数量")
    @NotNull(message = "商品数量不能为空")
    private Integer productNum;

    @ApiModelProperty("规格id")
    @NotNull(message = "规格id不能为空")
    private Integer productSkuId;

    @ApiModelProperty("属性")
    private String attr;

    @ApiModelProperty("加料")
    private String feed;

    @ApiModelProperty("包装费")
    @NotNull(message = "包装费不能为空")
    private BigDecimal bagPrice;

    @ApiModelProperty("商品价格")
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;

    @ApiModelProperty("划线价")
    @NotNull(message = "划线价不能为空")
    private BigDecimal productPrice;

    @ApiModelProperty("商品综合描述")
    private String describe;

    @ApiModelProperty("购物车类型0外卖1店内")
    @NotNull(message = "购物车类型不能为空")
    private Integer cartType;

    @ApiModelProperty("店铺id")
    @NotNull(message = "店铺id不能为空")
    private Integer shopSupplierId;
}
