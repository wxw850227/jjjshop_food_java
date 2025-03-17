package net.jjjshop.common.entity.order;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 购物车
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_cart")
@ApiModel(value = "Cart对象")
public class UserCart implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "cart_id", type = IdType.AUTO)
    private Integer cartId;

    @ApiModelProperty("会员id")
    private Integer userId;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("商品数量")
    private Integer productNum;

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

    @ApiModelProperty("店铺id")
    private Integer shopSupplierId;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
