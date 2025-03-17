package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderVirtualParam", description = "订单虚拟商品参数")
public class OrderVirtualParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单商品")
    private Integer orderId;

    @ApiModelProperty("虚拟商品发货信息")
    private String virtualContent;
}

