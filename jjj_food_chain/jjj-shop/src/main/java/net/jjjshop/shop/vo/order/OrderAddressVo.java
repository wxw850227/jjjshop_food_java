package net.jjjshop.shop.vo.order;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderAddress;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单收货地址VO")
public class OrderAddressVo extends OrderAddress {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("详细地址")
    private JSONObject region;
}
