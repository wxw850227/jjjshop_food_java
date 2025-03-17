

package net.jjjshop.shop.param.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CenterParam对象", description = "Center新增修改参数")
public class CenterParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("menu_id")
    private Integer menuId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("图片url")
    private String icon;

    @ApiModelProperty("排序(越小越靠前)")
    private Integer sort;

    @ApiModelProperty("跳转链接")
    private String path;

    @ApiModelProperty("链接名称")
    private String pathName;

    @ApiModelProperty("系统菜单标记")
    private String sysTag;

    @ApiModelProperty("1显示0隐藏")
    private Boolean isShow;
}
