package net.jjjshop.shop.param.setting;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "退货地址查询参数", description = "退货地址查询参数")
public class ReturnAddressParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退货地址id")
    private Integer addressId;

    @ApiModelProperty("收货人姓名")
    private String name;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("排序 (数字越小越靠前)")
    private Integer sort;
}
