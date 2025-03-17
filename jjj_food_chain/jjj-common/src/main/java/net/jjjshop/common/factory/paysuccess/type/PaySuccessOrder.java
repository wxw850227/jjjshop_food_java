package net.jjjshop.common.factory.paysuccess.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 返回订单信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("支付成功订单")
public class PaySuccessOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付状态")
    private Integer payStatus;

    @ApiModelProperty("应用id")
    private Integer appId;
}
