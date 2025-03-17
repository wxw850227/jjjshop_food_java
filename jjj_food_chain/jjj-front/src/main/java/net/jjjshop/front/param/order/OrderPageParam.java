package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderPageParam对象", description = "订单分页查询参数")
public class OrderPageParam extends BasePageOrderParam {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty("0所有,1进行中,2已完成")
    @NotNull(message = "dataType不能为空")
    private Integer dataType;

    @ApiModelProperty("用餐方式0外卖1店内")
    private Integer orderType;

    @ApiModelProperty("配送方式(10外卖配送 20上门取30打包带走40店内就餐")
    private Integer deliveryType;

    @ApiModelProperty("门店ID")
    private Integer shopSupplierId;

    @ApiModelProperty("用户Id")
    private Integer userId;

}
