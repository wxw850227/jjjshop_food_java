package net.jjjshop.common.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderParam对象", description = "订单参数")
public class OrderParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单Id")
    private Integer orderId;

    @ApiModelProperty("物流编号")
    private String expressNo;

    @ApiModelProperty("是否单包裹")
    private Boolean isSingle;

    @ApiModelProperty("是否电子面单发货")
    private Boolean isLabel;

    @ApiModelProperty("物流公司Id")
    private Integer expressId;

    @ApiModelProperty("电子面单模板ID")
    private Integer templateId;

    @ApiModelProperty("电子面单设置ID")
    private Integer labelSettingId;

    @ApiModelProperty("发货地址ID")
    private Integer addressId;

    @ApiModelProperty("是否取消")
    private Boolean isCancel;

    @ApiModelProperty("电子面单任务ID")
    private String taskId;

    @ApiModelProperty("appId")
    private Long appId;
}
