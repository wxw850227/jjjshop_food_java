package net.jjjshop.shop.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.Product;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品满减Vo")
public class ProductReduceVo extends Product {
    private static final long serialVersionUID = 1L;

    private Integer reducePid;

    @ApiModelProperty("商品图片主图")
    private String imagePath;

}
