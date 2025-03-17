

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
@ApiModel(value = "AppWx对象", description = "微信小程序登录参数")
public class AppWxParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("invitationId")
    private Integer invitationId;

    @NotEmpty(message = "code不能为空")
    @ApiModelProperty("code")
    private String code;

    @NotEmpty(message = "encryptedData不能为空")
    @ApiModelProperty("加密信息")
    private String encryptedData;

    @NotEmpty(message = "iv不能为空")
    @ApiModelProperty("iv")
    private String iv;

    @NotEmpty(message = "signature不能为空")
    @ApiModelProperty("signature")
    private String signature;

    @ApiModelProperty("推荐人id")
    private Integer refereeId;

    @NotEmpty(message = "管理员账号不能为空")
    @ApiModelProperty("管理员账号")
    private String source;

    @ApiModelProperty("token")
    private String token;

    @NotEmpty(message = "appId不能为空")
    @ApiModelProperty("appId")
    private Integer appId;

    @NotEmpty(message = "用户信息不能为空")
    @ApiModelProperty("userInfo")
    private String userInfo;

}
