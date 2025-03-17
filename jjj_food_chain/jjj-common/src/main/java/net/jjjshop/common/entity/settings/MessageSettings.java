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
 * 应用消息设置表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_message_settings")
@ApiModel(value = "MessageSettings对象")
public class MessageSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("设置id")
    @TableId(value = "message_settings_id", type = IdType.AUTO)
    private Integer messageSettingsId;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("消息id")
    private Integer messageId;

    @ApiModelProperty("0,未开启,1开启")
    private Integer smsStatus;

    @ApiModelProperty("短信模板(json)")
    private String smsTemplate;

    @ApiModelProperty("0,未开启,1开启")
    private Integer mpStatus;

    @ApiModelProperty("微信公众号模板(json)")
    private String mpTemplate;

    @ApiModelProperty("0,未开启,1开启")
    private Integer wxStatus;

    @ApiModelProperty("微信小程序模板(json)")
    private String wxTemplate;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
