package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("协议设置VO")
public class ProtocolVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //隐私协议
    private String privacy;
    //用户协议
    private String service;

    public ProtocolVo() {
        this.privacy = "";
        this.service = "";
    }

}
