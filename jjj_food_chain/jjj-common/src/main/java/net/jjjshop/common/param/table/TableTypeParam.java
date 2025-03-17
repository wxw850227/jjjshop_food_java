

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
import java.util.List;

/**
 * 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "TableTypeParam", description = "桌位类型新增修改参数")
public class TableTypeParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer typeId;

    @ApiModelProperty("桌位类型")
    @NotBlank(message = "类型名不能为空")
    private String typeName;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("最小人数")
    @NotNull(message = "最小人数不能为空")
    private Integer minNum;

    @ApiModelProperty("最大人数")
    @NotNull(message = "最大人数不能为空")
    private Integer maxNum;
}
