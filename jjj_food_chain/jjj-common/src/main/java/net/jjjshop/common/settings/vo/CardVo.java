package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("提货码VO")
public class CardVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String image;

    public CardVo(){
        this.image = "";
    }

}
