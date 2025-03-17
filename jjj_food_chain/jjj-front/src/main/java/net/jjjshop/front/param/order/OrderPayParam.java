

package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 微信小程序登录参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "订单参数对象", description = "订单参数对象")
public class OrderPayParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("orderId")
    private Integer orderId;

    @ApiModelProperty("支付来源")
    private String paySource;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("是否使用余额")
    private Boolean useBalance;

    @ApiModelProperty("订单类型，10商城订单，20礼包购订单，30余额充值订单，40预售定金订单，50预售尾款订单")
    private Integer orderType;
}
