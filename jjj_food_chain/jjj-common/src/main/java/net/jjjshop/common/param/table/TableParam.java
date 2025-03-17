

package net.jjjshop.common.param.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "TableTypeParam", description = "桌位类型新增修改参数")
public class TableParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer tableId;

    @ApiModelProperty("桌位编号")
    @NotBlank(message = "桌位编号不能为空")
    private String tableNo;

    @ApiModelProperty("区域id")
    @NotNull(message = "区域不能为空")
    private Integer areaId;

    @ApiModelProperty("类型id")
    @NotNull(message = "类型不能为空")
    private Integer typeId;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;


}
