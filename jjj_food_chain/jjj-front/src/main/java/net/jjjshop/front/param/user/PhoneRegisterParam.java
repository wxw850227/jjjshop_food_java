package net.jjjshop.front.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "AppWx对象", description = "微信小程序登录参数")
public class PhoneRegisterParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("注册来源")
    private String regSource;

    @ApiModelProperty("推荐人id")
    private Integer refereeId;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邀请好友")
    private Integer invitationId;

    @ApiModelProperty("appid")
    private Integer appId;
}
