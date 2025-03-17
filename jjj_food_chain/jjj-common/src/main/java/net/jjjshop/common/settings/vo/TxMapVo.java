package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("腾讯地图设置VO")
public class TxMapVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //腾讯地图KEY
    private String txKey;

    public TxMapVo(){
        this.txKey = "";
    }
}
