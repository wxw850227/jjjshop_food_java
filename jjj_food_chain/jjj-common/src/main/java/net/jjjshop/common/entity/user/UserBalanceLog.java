package net.jjjshop.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户余额变动明细表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_user_balance_log")
@ApiModel(value = "UserBalanceLog对象")
public class UserBalanceLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("余额变动场景(10用户充值 20用户消费 30管理员操作 40订单退款)")
    private Integer scene;

    @ApiModelProperty("变动金额")
    private BigDecimal money;

    @ApiModelProperty("描述/说明")
    private String description;

    @ApiModelProperty("管理员备注")
    private String remark;

    @ApiModelProperty("小程序商城id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
