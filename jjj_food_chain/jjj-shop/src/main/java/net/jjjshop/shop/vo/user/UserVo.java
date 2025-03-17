package net.jjjshop.shop.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.User;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("用户VO")
public class UserVo extends User {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("等级名称")
    private String gradeName;
}
