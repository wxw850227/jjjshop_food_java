package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderProduct;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单商品VO")
public class OrderProductVo extends OrderProduct {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片")
    private String imagePath;

}
