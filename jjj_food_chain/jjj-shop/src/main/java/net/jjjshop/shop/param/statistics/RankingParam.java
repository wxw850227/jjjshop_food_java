package net.jjjshop.shop.param.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderRankingParam对象", description = "OrderRankingParam对象")
public class RankingParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索时间")
    private String[] searchTime;

    @ApiModelProperty("开始搜索时间")
    private String startDate;

    @ApiModelProperty("结束搜索时间")
    private String endDate;

    @ApiModelProperty("搜索种类")
    private String type;

}
