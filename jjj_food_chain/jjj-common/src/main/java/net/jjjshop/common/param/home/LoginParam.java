

package net.jjjshop.common.param.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "LoginParam", description = "LoginParam")
public class LoginParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("验证码图片")
    private String codeImage;

    @ApiModelProperty("key值")
    private String codeKey;

    @ApiModelProperty("appId")
    private Integer appId;

}
