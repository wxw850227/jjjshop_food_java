

package net.jjjshop.shop.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProductParam对象", description = "商品查询参数")
public class ProductPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("tab栏选中的类型")
    private String type;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("0外卖1店内")
    private Integer productType;
}
