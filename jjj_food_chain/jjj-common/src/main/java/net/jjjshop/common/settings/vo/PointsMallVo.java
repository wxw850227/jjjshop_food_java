package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("积分商城VO")
public class PointsMallVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean isOpen;
    private Boolean isAgent;

    public PointsMallVo() {
        this.isOpen = false;
        this.isAgent = false;
    }

}
