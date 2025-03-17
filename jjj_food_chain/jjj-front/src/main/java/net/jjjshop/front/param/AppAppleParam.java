

package net.jjjshop.front.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 微信小程序登录参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "AppApple对象", description = "ios一键登录参数")
public class AppAppleParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("invitationId")
    private Integer invitationId;

    @NotEmpty(message = "openId不能为空")
    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("推荐人id")
    private Integer refereeId;

    @NotEmpty(message = "注册来源不能为空")
    @ApiModelProperty("注册来源")
    private String source;

    @NotEmpty(message = "appId不能为空")
    @ApiModelProperty("appId")
    private Integer appId;

}
