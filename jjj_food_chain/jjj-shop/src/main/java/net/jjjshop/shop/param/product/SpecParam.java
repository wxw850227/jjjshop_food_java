

package net.jjjshop.shop.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SpecParam对象", description = "Spec新增修改参数")
public class SpecParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Integer specId;

    @ApiModelProperty("规格组名称")
    @NotBlank(message = "规格组名称不能为空")
    private String specName;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
