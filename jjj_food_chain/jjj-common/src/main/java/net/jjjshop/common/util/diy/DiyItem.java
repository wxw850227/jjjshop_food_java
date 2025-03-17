package net.jjjshop.common.util.diy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * diy元素格式
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("diyItem")
public class DiyItem implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组件名称")
    private String name;

    @ApiModelProperty("组件类型")
    private String type;

    @ApiModelProperty("组件类别")
    private String group;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("参数个数")
    private Integer dataNum;

    @ApiModelProperty("参数")
    private JSONObject params;

    @ApiModelProperty("样式")
    private JSONObject style;

    @ApiModelProperty("选择的数据")
    private JSONArray data;

    @ApiModelProperty("默认数据")
    private JSONArray defaultData;
}
