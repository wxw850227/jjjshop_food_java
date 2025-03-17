package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("满额满减VO")
public class FullFreeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer isOpen;
    private Integer money;

    public FullFreeVo() {
        this.isOpen = 0;
        this.money = 0;
    }
}
