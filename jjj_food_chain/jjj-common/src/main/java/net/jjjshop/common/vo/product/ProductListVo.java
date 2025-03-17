

package net.jjjshop.common.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.Product;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品列表VO")
public class ProductListVo extends Product {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("销量")
    private Integer productSales;

    @ApiModelProperty("图片")
    private String productImage;

    @ApiModelProperty("是否活动商品")
    private Boolean isActivity;

    @ApiModelProperty("图片")
    private List<ProductImageVo> image;

    @ApiModelProperty("商品sku")
    private List<ProductSkuVo> skuList;

    @ApiModelProperty("商品sku")
    private ProductSkuVo productSku;

    @ApiModelProperty("购物车数量")
    private Integer cartNum;

    @ApiModelProperty("产品属性(10单属性 20多属性)")
    private Integer specTypes;
}
