package net.jjjshop.shop.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.settings.Message;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("消息模板VO")
public class MessageVo extends Message {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息类型")
    private String messageTypeText;

    @ApiModelProperty("微信公众号开启状态")
    private Integer mpStatus;

    @ApiModelProperty("微信小程序开启状态")
    private Integer wxStatus;

    @ApiModelProperty("短信开启状态")
    private Integer smsStatus;

    @ApiModelProperty("消息设置id")
    private Integer messageSettingsId;

    @ApiModelProperty("公众号消息模板")
    private String mpTemplate;

    @ApiModelProperty("小程序消息模板")
    private String wxTemplate;

    @ApiModelProperty("短信消息模板")
    private String smsTemplate;
}
