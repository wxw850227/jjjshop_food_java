

package net.jjjshop.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "MessageParam对象", description = "message新增修改参数")
public class MessageParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer messageId;

    @NotBlank(message = "消息名称不能为空")
    @ApiModelProperty("消息名称")
    private String messageName;

    @NotBlank(message = "消息英文名不能为空")
    @ApiModelProperty("消息英文名")
    private String messageEname;

    @NotNull(message = "通知对象不能为空")
    @ApiModelProperty("通知对象,10会员20,商家")
    private Integer messageTo;

    @NotNull(message = "消息类别不能为空")
    @ApiModelProperty("消息类别,10订单20分销")
    private Integer messageType;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("消息排序")
    private Integer sort;
}
