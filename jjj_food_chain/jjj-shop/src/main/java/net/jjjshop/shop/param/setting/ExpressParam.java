package net.jjjshop.shop.param.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "物流公司查询参数", description = "物流公司查询参数")
public class ExpressParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物流公司id")
    private Integer expressId;

    @ApiModelProperty("物流公司名称")
    private String expressName;

    @ApiModelProperty("物流公司代码 (快递100)")
    private String expressCode;

    @ApiModelProperty("微信小程序物流公司编码")
    private String wxCode;

    @ApiModelProperty("排序 (数字越小越靠前)")
    private Integer sort;
}
