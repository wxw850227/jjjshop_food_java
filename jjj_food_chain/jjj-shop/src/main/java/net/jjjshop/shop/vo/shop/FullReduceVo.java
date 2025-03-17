package net.jjjshop.shop.vo.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("用户登陆日志VO")
public class FullReduceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer fullreduceId;

    @ApiModelProperty("活动名称")
    private String activeName;

    @ApiModelProperty("满类型1，满额2，满件数")
    private Integer fullType;

    @ApiModelProperty("满值")
    private Integer fullValue;

    @ApiModelProperty("减类型，1，减金额 2，打折")
    private Integer reduceType;

    @ApiModelProperty("减值")
    private Integer reduceValue;

    @ApiModelProperty("商品id")
    private Integer productId;
}
