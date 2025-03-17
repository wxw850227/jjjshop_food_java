

package net.jjjshop.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("WxPayResult")
public class WxPayResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("mchId")
    private String mchId;

    @ApiModelProperty("appId")
    private String appId;

}
