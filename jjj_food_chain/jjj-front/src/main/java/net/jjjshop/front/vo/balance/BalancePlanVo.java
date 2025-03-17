package net.jjjshop.front.vo.balance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("余额充值VO")
public class BalancePlanVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer planId;

    @ApiModelProperty("套餐名称")
    private String planName;

    @ApiModelProperty("充值金额")
    private Integer money;

    @ApiModelProperty("赠送金额")
    private Integer giveMoney;

    @ApiModelProperty("到账金额")
    private Integer realMoney;

    @ApiModelProperty("排序(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

}
