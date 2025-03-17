package net.jjjshop.front.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.front.vo.supplier.SupplierVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("订单列表VO")
public class OrderListVo  extends Order {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("后台修改的订单金额（差价）类型")
    private String updatePriceSymbol;

    @ApiModelProperty("商品满减总额")
    private BigDecimal productReduceMoney;

    @ApiModelProperty("支付方式(10余额支付 20微信支付)文本")
    private String payTypeText;

    @ApiModelProperty("付款状态(10未付款 20已付款)文本")
    private String payStatusText;

    @ApiModelProperty("配送方式(10快递配送 20上门自提 30无需物流)文本")
    private String deliveryTypeText;

    @ApiModelProperty("发货状态(10未发货 20已发货)文本")
    private String deliveryStatusText;

    @ApiModelProperty("收货状态(10未收货 20已收货)文本")
    private String receiptStatusText;

    @ApiModelProperty("订单状态10=>进行中，20=>已经取消，30=>已完成文本")
    private String orderStatusText;

    @ApiModelProperty("订单类型")
    private String orderLabel;

    @ApiModelProperty("订单来源(10普通 20积分 30拼团 40砍价 50秒杀 60礼包购)文本")
    private String orderSourceText;

    @ApiModelProperty("订单商品列表")
    private List<OrderProductVo> product;

    @ApiModelProperty("商品数量")
    private Integer totalNum;

    @ApiModelProperty("微信商户号id")
    private String mchId;

    @ApiModelProperty("门店详情")
    private SupplierVo supplier;

}
