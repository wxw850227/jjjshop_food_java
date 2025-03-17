

package net.jjjshop.front.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 微信小程序登录参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ProductParam对象", description = "商品参数对象")
public class ProductParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索关键字")
    private String search;

    @ApiModelProperty("商品分类")
    private Integer categoryId;

    @ApiModelProperty("排序方式")
    private String sortType;

    @ApiModelProperty("价格排序方式")
    private Integer sortPrice;
}
