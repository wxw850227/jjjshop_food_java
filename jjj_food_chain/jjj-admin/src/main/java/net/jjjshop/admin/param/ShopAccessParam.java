

package net.jjjshop.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 管理员重置用户密码参数
 **/
@Data
@Accessors(chain = true)
@ApiModel("权限菜单参数")
public class ShopAccessParam implements Serializable {

    private static final long serialVersionUID = 5364321420976152005L;

    @ApiModelProperty("主键id")
    private Integer accessId;

    @NotEmpty(message = "权限名称不能为空")
    @ApiModelProperty("权限名称")
    private String name;

    @NotEmpty(message = "路由地址不能为空")
    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("父级id")
    private Integer parentId;

    @ApiModelProperty("排序(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("重定向名称")
    private String redirectName;

    @ApiModelProperty("是否是路由 0=不是1=是")
    private Integer isRoute;

    @ApiModelProperty("是否是菜单 0不是 1是")
    private Integer isMenu;

    @ApiModelProperty("别名(废弃)")
    private String alias;

    @ApiModelProperty("是否显示1=显示0=不显示")
    private Integer isShow;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("是否门店菜单0否1是")
    private Integer isSupplier;

}
