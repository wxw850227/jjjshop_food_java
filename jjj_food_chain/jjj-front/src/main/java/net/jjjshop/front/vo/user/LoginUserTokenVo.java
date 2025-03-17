

package net.jjjshop.front.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.shiro.service.LoginToken;
import net.jjjshop.framework.shiro.vo.LoginUserVo;

@Data
@Accessors(chain = true)
@ApiModel("登录用户信息TokenVO")
public class LoginUserTokenVo implements LoginToken {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("token")
    private String token;

    /**
     * 登录用户对象
     */
    private LoginUserVo loginUserVo;
}
