

package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.front.util.order.vo.OrderData;
import net.jjjshop.front.util.order.vo.OrderSource;
import net.jjjshop.front.util.order.vo.SettledRule;
import net.jjjshop.front.vo.product.ProductBuyVo;

import java.io.Serializable;
import java.util.List;

/**
 * 微信小程序登录参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "创建订单参数对象", description = "创建订单参数对象")
public class OrderCreateParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户")
    private User user;

    @ApiModelProperty("商品信息")
    private List<ProductBuyVo> productList;

    @ApiModelProperty("参数")
    private OrderBuyParam orderBuyParam;

    @ApiModelProperty("订单来源")
    private OrderSource orderSource;

    @ApiModelProperty("结算规则")
    private SettledRule settledRule;

    @ApiModelProperty("订单数据")
    private OrderData orderData;

}
