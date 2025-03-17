package net.jjjshop.front.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * 商品评价 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CommentParam对象", description = "商品评价参数")
public class CommentParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评价参数")
    private List<CommentListParam> params;

    @ApiModelProperty("订单Id")
    private Integer orderId;
}
