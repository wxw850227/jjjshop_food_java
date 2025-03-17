

package net.jjjshop.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.settings.MessageField;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "AppParam对象", description = "app新增修改参数")
public class FieldParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "消息id不能为空")
    @ApiModelProperty("messageId")
    private Integer messageId;

    @NotNull(message = "字段不能为空")
    @ApiModelProperty("字段集合")
    private List<MessageField> fieldData;

    @ApiModelProperty("删除的字段id集合")
    private Integer[] deleteIds;
}
