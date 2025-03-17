

package net.jjjshop.framework.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Add;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 登录用户对象，响应给前端
 **/
@Data
@Accessors(chain = true)
public class LoginShopUserVo implements Serializable {

    private static final long serialVersionUID = -1758338570596088158L;

    @ApiModelProperty("主键")
    @NotNull(message = "ID不能为空", groups = {Update.class})
    private Integer shopUserId;

    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不能为空", groups = {Add.class})
    private String userName;

    @ApiModelProperty("是否为超级管理员0不是,1是")
    private Boolean isSuper;

    @ApiModelProperty("总店id")
    private Integer shopSupplierId;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("权限列表-后端")
    private Set<String> permissions;

    @ApiModelProperty("权限列表-前端菜单")
    private List<LoginShopAccessVo> menus;

}
