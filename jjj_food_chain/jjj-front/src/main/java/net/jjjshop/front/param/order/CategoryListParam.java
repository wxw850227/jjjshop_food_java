package net.jjjshop.front.param.order;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "商品分类参数对象", description = "商品分类对象")
public class CategoryListParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("0外卖1店内")
    private Integer type;

    @ApiModelProperty("门店ID")
    private Integer shopSupplierId;

    @ApiModelProperty("坐标经度")
    private String longitude;

    @ApiModelProperty("坐标纬度")
    private String latitude;

    @ApiModelProperty("tableId")
    private Integer tableId;

    @ApiModelProperty("配送方式(10外卖配送 20上门取30打包带走40店内就餐")
    private Integer delivery;

    @ApiModelProperty("用餐方式0外卖1店内")
    private Integer orderType;

    private Integer userId;

    private Integer productId;
}
