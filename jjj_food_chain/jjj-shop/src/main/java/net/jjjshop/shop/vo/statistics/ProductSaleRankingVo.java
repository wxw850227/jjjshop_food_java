package net.jjjshop.shop.vo.statistics;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderProduct;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("商品信息排行VO")
public class ProductSaleRankingVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("总销售额")
    private BigDecimal salesVolume;

    @ApiModelProperty("总销售额")
    private BigDecimal totalPrice;

    @ApiModelProperty("总销售次数")
    private Integer totalNum;

    @ApiModelProperty("商品图片")
    private String imagePath;

    @ApiModelProperty("浏览次数")
    private Integer viewTimes;

    @ApiModelProperty("商品ID")
    private Integer productId;

}
