package net.jjjshop.front.vo.balance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("用户余额明细VO")
public class UserBalanceLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("log_id")
    private Integer logId;

    @ApiModelProperty("user_id")
    private Integer userId;

    @ApiModelProperty("scene")
    private Integer scene;

    @ApiModelProperty("sceneText")
    private String sceneText;

    @ApiModelProperty("money")
    private BigDecimal money;

    @ApiModelProperty("description")
    private String description;

    @ApiModelProperty("remark")
    private String remark;

    @ApiModelProperty("create_time")
    private Date createTime;
}
