package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("主题设置VO")
public class ThemeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer theme;

    public ThemeVo() {
        this.theme = 0;
    }

}
