

package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import java.util.List;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderPageParam对象", description = "订单分页参数")
public class OrderPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("搜索订单号")
    private String orderNo;

    @ApiModelProperty("查询条件：起始日期")
    private String startDate;

    @ApiModelProperty("查询条件：结束日期")
    private String endDate;

    @ApiModelProperty("门店ID")
    private Integer shopSupplierId;

    @ApiModelProperty("10商家配送20达达30配送员40美团50UU跑腿60蜂鸟")
    private Integer deliverSource;

    @ApiModelProperty("配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)")
    private Integer deliverStatus;


    @ApiModelProperty("查询条件：配送方式,10外卖配送,20外卖自提,30店内打包40店内堂食")
    private Integer styleId;

    @ApiModelProperty("查询条件：订单状态")
    private String dataType;

    @ApiModelProperty("用餐方式0外卖1店内")
    private Integer orderType;

    @ApiModelProperty("配送公司(10商家配送20达达30配送员)")
    private Integer deliversource;

    @ApiModelProperty("0外卖1店内")
    private Integer productType;

    @ApiModelProperty("查询日期")
    private List<String> createTime;

    @ApiModelProperty("商品销量时间,1=本日,2=最近7天,3=本月,4=本年")
    private Integer productTime;

    @ApiModelProperty("商品销售额时间,1=本日,2=最近7天,3=本月,4=本年")
    private Integer productSaleTime;
}
