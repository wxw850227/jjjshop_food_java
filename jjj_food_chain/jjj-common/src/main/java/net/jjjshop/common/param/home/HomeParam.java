

package net.jjjshop.common.param.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "HomeParam", description = "HomeParam")
public class HomeParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品销量时间,1=本日,2=最近7天,3=本月,4=本年")
    private Integer productTime;

    @ApiModelProperty("商品销售额时间,1=本日,2=最近7天,3=本月,4=本年")
    private Integer productSaleTime;

    @ApiModelProperty("订单时间,1=本日,2=最近7天,3=本月,4=本年")
    private Integer orderTime;

    @ApiModelProperty("支付金额时间,1=本日,2=最近7天,3=最近15天,4=最近30天")
    private Integer saleTime;

    @ApiModelProperty("商户ID")
    private Integer shopSupplierId;

    @ApiModelProperty("appId")
    private Integer appId;

    @ApiModelProperty("商品销量时间,1=近一周,2=最近30天,3=本月,4=本年")
    private Integer time;

    @ApiModelProperty("商品销量排序,1销量正序,2销量倒序,3销售额正序,4销售额倒序")
    private Integer type;

    @ApiModelProperty("统计概况时间,1=本日,2=昨日,3=7日内,4=自定义")
    private Integer searchTimeType;

    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

}
