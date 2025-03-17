package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderExtract;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.vo.order.OrderProductVo;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单详情VO")
public class OrderVo extends Order {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购买用户")
    private String nickName;

    private User user;

    @ApiModelProperty("微信头像")
    private String avatarUrl;

    @ApiModelProperty("订单来源")
    private String orderSourceText;

    @ApiModelProperty("订单状态")
    private String orderStatusText;

    private Supplier supplier;

    @ApiModelProperty("订单类型")
    private String orderTypeText;

    @ApiModelProperty("支付方式")
    private String payTypeText;

    @ApiModelProperty("配送方式")
    private String deliveryTypeText;

    @ApiModelProperty("支付状态文本")
    private String payStatusText;

    @ApiModelProperty("发货状态文本")
    private String deliveryStatusText;

    @ApiModelProperty("收货状态(10未收货 20已收货)文本")
    private String receiptStatusText;

    @ApiModelProperty("购买商品")
    private List<OrderProductVo> product;

    @ApiModelProperty("地址详情")
    private OrderAddressVo address;

    @ApiModelProperty("自提详情")
    private OrderExtract extract;

    @ApiModelProperty("物流公司名称")
    private String expressName;

    @ApiModelProperty("用户手机号")
    private String mobile;

    @ApiModelProperty("门店名称")
    private String supplierName;

}
