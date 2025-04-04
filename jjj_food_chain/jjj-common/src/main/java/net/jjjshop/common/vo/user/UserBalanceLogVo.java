package net.jjjshop.common.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.UserBalanceLog;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("用户余额明细VO")
public class UserBalanceLogVo extends UserBalanceLog {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("微信头像")
    private String avatarUrl;
}
