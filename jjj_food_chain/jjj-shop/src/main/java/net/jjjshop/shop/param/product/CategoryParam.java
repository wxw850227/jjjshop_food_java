

package net.jjjshop.shop.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CategoryParam对象", description = "Category新增修改参数")
public class CategoryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("主键id")
    private Integer categoryId;

    @NotEmpty(message = "分类名称不能为空")
    @ApiModelProperty("分类名称")
    private String name;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty("排序")
    private Integer sort;

    @NotNull(message = "分类图片不能为空")
    @ApiModelProperty("分类图片")
    private Integer imageId;

    @ApiModelProperty("0外卖1店内")
    private Integer type;

    @ApiModelProperty("0普通1特殊")
    private Integer isSpecial;
}
