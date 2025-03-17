package net.jjjshop.common.param.order;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 模板参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "模板参数对象", description = "模板参数对象")
public class SignParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付渠道来源")
    private String paySource;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("appId")
    private Long appId;


    @ApiModelProperty("pageId")
    private Integer pageId;
}
