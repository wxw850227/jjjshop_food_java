package net.jjjshop.shop.param.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "消息字段配置参数", description = "消息字段配置参数")
public class MessageSettingsParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息id")
    private Integer messageId;

    @ApiModelProperty("消息类型")
    private String messageType;

    @ApiModelProperty("消息模板id,第三方申请")
    private String templateId;

    @NotNull(message = "字段明细不能为空")
    @ApiModelProperty("字段明细)")
    private List<FieldParam> fieldList;

    @Data
    @Accessors(chain = true)
    @ApiModel("字段明细")
    public static class FieldParam implements Serializable{
        private static final long serialVersionUID = 1L;
        private Integer messageFieldId;
        private Integer messageId;
        private String fieldName;
        private String fieldEname;
        private String filedValue;
        private Boolean isVar;
        private String fieldNewEname;
        private String filedNewValue;
        public FieldParam(){

        }
    }
}
