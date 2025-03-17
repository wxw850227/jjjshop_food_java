

package net.jjjshop.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("地区VO")
public class RegionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("父id")
    private Integer pid;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("层级 1 2 3 省市区县")
    private Integer level;

    @ApiModelProperty("children")
    private List<RegionVo> children;

    @ApiModelProperty("是否选中")
    private Boolean checked;

    @ApiModelProperty("选中下标")
    private List<Integer> index;

    @ApiModelProperty("选中下标")
    private Integer childIndex;
}
