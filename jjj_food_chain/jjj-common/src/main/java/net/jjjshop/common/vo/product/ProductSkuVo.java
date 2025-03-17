package net.jjjshop.common.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.ProductSku;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品分类VO")
public class ProductSkuVo extends ProductSku {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片主图")
    private String imagePath;

    @ApiModelProperty("多规格文字")
    private String productAttr;
}
