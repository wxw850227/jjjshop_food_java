package net.jjjshop.common.vo.statistics;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderProduct;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("商品信息排行VO")
public class ProductSaleRankingVo extends OrderProduct {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总销售额")
    private BigDecimal salesVolume;

    @ApiModelProperty("商品图片")
    private String imagePath;

    @ApiModelProperty("浏览次数")
    private Integer viewTimes;

}
