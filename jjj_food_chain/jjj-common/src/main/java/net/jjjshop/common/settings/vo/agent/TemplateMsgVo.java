package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("模板消息")
public class TemplateMsgVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String applyTpl;
    private String cashTpl;

    public TemplateMsgVo() {
        this.applyTpl = "";
        this.cashTpl = "";
    }

}
