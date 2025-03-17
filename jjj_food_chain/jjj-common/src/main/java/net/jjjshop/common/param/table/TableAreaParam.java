

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
@ApiModel(value = "TableAreaParam", description = "桌位区域新增修改参数")
public class TableAreaParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "area_id", type = IdType.AUTO)
    private Integer areaId;

    @ApiModelProperty("区域名称")
    @NotBlank(message = "类型名不能为空")
    private String areaName;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
