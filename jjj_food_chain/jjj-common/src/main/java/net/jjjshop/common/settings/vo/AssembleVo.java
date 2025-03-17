package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("交易设置VO")
public class AssembleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 是否开启
    private Boolean isOpen;
    // 是否开启积分
    private Boolean isPoint;
    // 是否开启分销
    private Boolean isAgent;

    public AssembleVo() {
        this.isOpen = false;
        this.isPoint = false;
        this.isAgent = false;
    }
}
