package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单导出VO")
public class OrderExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("商品总金额(不含优惠折扣)")
    private BigDecimal totalPrice;

    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponMoney;

    @ApiModelProperty("积分抵扣金额")
    private BigDecimal pointsMoney;

    @ApiModelProperty("实际付款金额(包含运费)")
    private BigDecimal payPrice;

    @ApiModelProperty("买家留言")
    private String buyerRemark;

    @ApiModelProperty("付款时间")
    private Date payTime;

    @ApiModelProperty("运费金额")
    private BigDecimal expressPrice;

    @ApiModelProperty("物流单号")
    private String expressNo;

    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    @ApiModelProperty("收货时间")
    private Date receiptTime;

    @ApiModelProperty("微信支付交易号")
    private String transactionId;

    @ApiModelProperty("是否已评价(0否 1是)")
    private String isCommentText;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("购买用户")
    private String nickName;

    @ApiModelProperty("订单状态")
    private String orderStatusText;

    @ApiModelProperty("配送方式")
    private String deliveryTypeText;

    @ApiModelProperty("支付状态文本")
    private String payStatusText;

    @ApiModelProperty("发货状态文本")
    private String deliveryStatusText;

    @ApiModelProperty("购买商品")
    private String product;

    @ApiModelProperty("发货物流公司名称")
    private String expressName;

    @ApiModelProperty("详细地址")
    private String detailRegion;

    @ApiModelProperty("后台更新价格类型")
    private String updatePriceSymbol;

    @ApiModelProperty("收货状态(10未收货 20已收货)文本")
    private String receiptStatusText;

    @ApiModelProperty("收货人姓名")
    private String addressName;

    @ApiModelProperty("收货人电话")
    private String addressPhone;

    @ApiModelProperty("核销门店")
    private String extractStoreName;

    @ApiModelProperty("联系人")
    private String extractLinkman;

    @ApiModelProperty("联系电话")
    private String extractPhone;

    @ApiModelProperty("支付方式")
    private String payTypeText;

}
