

package net.jjjshop.shop.param.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "UnitParam对象", description = "UnitParam新增修改参数")
public class UnitParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Integer unitId;

    @ApiModelProperty("属性名")
    @NotBlank(message = "属性名不能为空")
    private String unitName;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
