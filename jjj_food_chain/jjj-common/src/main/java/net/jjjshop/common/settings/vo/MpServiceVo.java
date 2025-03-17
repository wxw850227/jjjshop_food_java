package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("客服设置VO")
public class MpServiceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String qq;
    private String wechat;
    private String mpImage;

    public MpServiceVo() {
        this.qq = "123456";
        this.wechat = "123456";
        this.mpImage = "";
    }
}
