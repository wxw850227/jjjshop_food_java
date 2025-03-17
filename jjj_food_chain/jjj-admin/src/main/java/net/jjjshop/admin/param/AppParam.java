

package net.jjjshop.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Add;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "AppParam对象", description = "app新增修改参数")
public class AppParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("appId")
    private Integer appId;

    @NotEmpty(message = "应用名称不能为空")
    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("是否回收")
    private Boolean isRecycle;

    @ApiModelProperty("是否过期")
    private Boolean noExpire;

    @ApiModelProperty("过期时间")
    private String expireTime;

    @NotEmpty(message = "管理员账号不能为空")
    @ApiModelProperty("管理员账号")
    private String userName;

    @NotEmpty(message = "登录密码不能为空", groups = {Add.class})
    @ApiModelProperty("登录密码")
    private String password;
}
