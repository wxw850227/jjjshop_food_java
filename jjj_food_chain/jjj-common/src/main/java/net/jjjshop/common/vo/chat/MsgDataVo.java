package net.jjjshop.common.vo.chat;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("msgDataVo")
public class MsgDataVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息类型,ping,msg,close")
    private String type;

    @ApiModelProperty("消息类型,0:文本,1：图片,2：商品，3：订单")
    private Integer msgType;

    @ApiModelProperty("发送类型，1客服发，2，用户发")
    private Integer sendType;

    @ApiModelProperty("谁发送")
    private Integer from;

    @ApiModelProperty("谁接收")
    private Integer to;

    @ApiModelProperty("发送内容")
    private String content;

    @ApiModelProperty("对方是否在线")
    private Integer status;
}
