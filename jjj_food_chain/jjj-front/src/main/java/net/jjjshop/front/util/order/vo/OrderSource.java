package net.jjjshop.front.util.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "订单来源", description = "订单来源")
public class OrderSource implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动id")
    private Integer activityId;

    @ApiModelProperty("source")
    private Integer source;

    public OrderSource(){
        this.source = 0;
        this.activityId = 0;
    }
}
