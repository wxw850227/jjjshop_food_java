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
 * 应用消息表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_message")
@ApiModel(value = "Message对象")
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("消息id")
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    @ApiModelProperty("消息名称")
    private String messageName;

    @ApiModelProperty("消息英文名")
    private String messageEname;

    @ApiModelProperty("通知对象,10会员20,商家")
    private Integer messageTo;

    @ApiModelProperty("消息类别,10订单20分销")
    private Integer messageType;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("消息排序")
    private Integer sort;

    @ApiModelProperty("0,否1,是")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
