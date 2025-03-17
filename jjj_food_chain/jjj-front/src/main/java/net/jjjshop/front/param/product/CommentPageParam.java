package net.jjjshop.front.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 商品评价 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CommentPageParam对象", description = "商品评价分页参数对象")
public class CommentPageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品id")
    private Integer productId;

    @ApiModelProperty("评分 (10好评 20中评 30差评)")
    private Integer score;

    @ApiModelProperty("用户id")
    private Integer userId;
}
