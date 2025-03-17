package net.jjjshop.front.util.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "订单结算规则", description = "订单结算规则")
public class SettledRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("优惠券抵扣")
    private Boolean isCoupon;

    @ApiModelProperty("是否使用积分抵扣")
    private Boolean isUsePoints;

    @ApiModelProperty("强制使用积分，积分兑换")
    private Boolean forcePoints;

    @ApiModelProperty("会员等级折扣")
    private Boolean isUserGrade;

    @ApiModelProperty("商品是否开启分销,最终还是支付成功后判断分销活动是否开启")
    private Boolean isAgent;

    public SettledRule(){
        this.isCoupon = true;
        this.isUsePoints = true;
        this.forcePoints = false;
        this.isUserGrade = true;
        this.isAgent = true;
    }
}
