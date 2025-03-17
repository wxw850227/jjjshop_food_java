package net.jjjshop.shop.param.order;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderParam对象", description = "订单参数")
public class OrderPriceParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单Id")
    private Integer orderId;

    @ApiModelProperty("订单金额")
    private BigDecimal updatePrice;

    @ApiModelProperty("运费金额")
    private BigDecimal updateExpressPrice;

}
