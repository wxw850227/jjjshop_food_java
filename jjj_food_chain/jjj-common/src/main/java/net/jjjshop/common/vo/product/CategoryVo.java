package net.jjjshop.common.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.ProductCategory;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品分类VO")
public class CategoryVo extends ProductCategory {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类图片路径")
    private String imagePath;

    @ApiModelProperty("子分类集合")
    private List<CategoryVo> children;

    @ApiModelProperty("父类名称")
    private String parent;

    @ApiModelProperty("商品")
    private List<ProductListVo> products;
}
