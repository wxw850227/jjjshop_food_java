package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderRefundPageParam对象", description = "售后单分页参数")
public class OrderRefundPageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索订单号")
    private String orderNo;

    @ApiModelProperty("查询条件：起始日期")
    private String startDate;

    @ApiModelProperty("查询条件：结束日期")
    private String endDate;

    @ApiModelProperty("售后类型")
    private Integer type;

    @ApiModelProperty("售后单状态(选项卡)")
    private Integer status;
}
