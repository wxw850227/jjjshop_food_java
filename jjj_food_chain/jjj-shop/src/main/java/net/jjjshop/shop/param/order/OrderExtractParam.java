package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderExtractParam对象", description = "订单核销参数")
public class OrderExtractParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单Id")
    private Integer orderId;
}
