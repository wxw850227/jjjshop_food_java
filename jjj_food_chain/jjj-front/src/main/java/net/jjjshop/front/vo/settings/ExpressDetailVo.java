package net.jjjshop.front.vo.settings;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("物流详情VO")
public class ExpressDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物流公司id")
    private Integer expressId;

    @ApiModelProperty("物流公司名称")
    private String expressName;

    @ApiModelProperty("物流编号")
    private String expressNo;

    @ApiModelProperty("快递100物流实时信息")
    private JSONObject express;
}
