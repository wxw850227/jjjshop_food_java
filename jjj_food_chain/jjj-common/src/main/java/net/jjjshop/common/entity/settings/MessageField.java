package net.jjjshop.common.entity.settings;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 应用消息字段表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_message_field")
@ApiModel(value = "MessageField对象")
public class MessageField implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("字段id")
    @TableId(value = "message_field_id", type = IdType.AUTO)
    private Integer messageFieldId;

    @ApiModelProperty("消息id")
    private Integer messageId;

    @ApiModelProperty("字段名称")
    private String fieldName;

    @ApiModelProperty("字段英文名")
    private String fieldEname;

    @ApiModelProperty("字段默认")
    private String filedValue;

    @ApiModelProperty("是否变量.0,否,1是")
    private Boolean isVar;

    @ApiModelProperty("字段排序")
    private Integer sort;

    @ApiModelProperty("是否删除")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
