package net.jjjshop.front.vo.order;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.vo.user.UserAddressVo;
import net.jjjshop.front.vo.settings.ExpressListVo;
import net.jjjshop.front.vo.supplier.SupplierVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("订单详情VO")
public class OrderDetailVo extends Order {

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("商品总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty("订单总金额")
    private BigDecimal orderPrice;

    @ApiModelProperty("优惠券id")
    private Integer couponId;

    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponMoney;

    @ApiModelProperty("系统优惠券")
    private Integer couponIdSys;

    @ApiModelProperty("平台优惠券抵扣")
    private BigDecimal couponMoneySys;

    @ApiModelProperty("积分抵扣金额")
    private BigDecimal pointsMoney;

    @ApiModelProperty("积分抵扣数量")
    private BigDecimal pointsNum;

    @ApiModelProperty("实际付款金额(包含运费)")
    private BigDecimal payPrice;

    @ApiModelProperty("后台修改的订单金额（差价）")
    private BigDecimal updatePrice;

    @ApiModelProperty("满减金额")
    private BigDecimal fullreduceMoney;

    @ApiModelProperty("满减备注")
    private String fullreduceRemark;

    @ApiModelProperty("买家留言")
    private String buyerRemark;

    @ApiModelProperty("支付方式(10余额支付 20微信支付)")
    private Integer payType;

    @ApiModelProperty("支付来源,mp,wx")
    private String paySource;

    @ApiModelProperty("付款状态(10未付款 20已付款)")
    private Integer payStatus;

    @ApiModelProperty("付款时间")
    private Date payTime;

    @ApiModelProperty("支付截止时间")
    private Date payEndTime;

    @ApiModelProperty("配送方式(10外卖配送 20上门取30打包带走40店内就餐")
    private Integer deliveryType;

    @ApiModelProperty("自提门店id")
    private Integer extractStoreId;

    @ApiModelProperty("核销店员id")
    private Integer extractClerkId;

    @ApiModelProperty("运费金额")
    private BigDecimal expressPrice;

    @ApiModelProperty("物流公司id")
    private Integer expressId;

    @ApiModelProperty("物流公司")
    private String expressCompany;

    @ApiModelProperty("物流单号")
    private String expressNo;

    @ApiModelProperty("发货状态(10未发货 20已发货)")
    private Integer deliveryStatus;

    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    @ApiModelProperty("收货状态(10未收货 20已收货)")
    private Integer receiptStatus;

    @ApiModelProperty("收货时间")
    private Date receiptTime;

    @ApiModelProperty("订单状态10=>进行中，20=>已经取消，30=>已完成")
    private Integer orderStatus;

    @ApiModelProperty("赠送的积分数量")
    private BigDecimal pointsBonus;

    @ApiModelProperty("订单是否已结算(0未结算 1已结算)")
    private Integer isSettled;

    @ApiModelProperty("微信支付交易号")
    private String transactionId;

    @ApiModelProperty("是否已评价(0否 1是)")
    private Integer isComment;

    @ApiModelProperty("订单来源(10普通 20收银台)")
    private Integer orderSource;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("拼团等活动失败退款")
    private Integer isRefund;

    @ApiModelProperty("拼团状态 10拼单中 20拼单成功 30拼单失败")
    private Integer assembleStatus;

    @ApiModelProperty("活动id，对应拼团，秒杀，砍价活动id")
    private Integer activityId;

    @ApiModelProperty("是否可以分销0否1是")
    private Integer isAgent;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("供应商结算金额,支付金额-平台结算金额")
    private BigDecimal supplierMoney;

    @ApiModelProperty("平台结算金额")
    private BigDecimal sysMoney;

    @ApiModelProperty("直播间id")
    private Integer roomId;

    @ApiModelProperty("商家取消订单备注")
    private String cancelRemark;

    @ApiModelProperty("是否自动发货1自动0手动")
    private Integer virtualAuto;

    @ApiModelProperty("虚拟物品内容")
    private String virtualContent;

    @ApiModelProperty("包装费")
    private BigDecimal bagPrice;

    @ApiModelProperty("送餐时间")
    private String mealtime;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("对账id")
    private Integer financeId;

    @ApiModelProperty("就餐桌号")
    private String tableNo;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("10商家配送20达达30配送员")
    private Integer deliverSource;

    @ApiModelProperty("配送状态，待接单＝1,待取货＝2,配送中＝3,已完成＝4")
    private Integer deliverStatus;

    @ApiModelProperty("配送员id")
    private Integer driverId;

    @ApiModelProperty("骑手服务费")
    private BigDecimal takeFee;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountMoney;

    @ApiModelProperty("收银员id")
    private Integer cashierId;

    @ApiModelProperty("店内用处类型10堂食20快餐")
    private Boolean eatType;

    @ApiModelProperty("桌位id")
    private Integer tableId;

    @ApiModelProperty("就餐人数")
    private Integer mealNum;

    @ApiModelProperty("服务费")
    private BigDecimal serviceMoney;

    @ApiModelProperty("服务费类型0按就餐人数1按桌台收费")
    @TableField("serviceType")
    private Boolean servicetype;

    @ApiModelProperty("计算模式10先结账后用餐20先用餐后结账")
    private Integer settleType;

    @ApiModelProperty("0定时清台1立即清台")
    private Boolean autoClose;

    @ApiModelProperty("清台时间")
    private Date closeTime;

    @ApiModelProperty("余额抵扣金额")
    private BigDecimal balance;

    @ApiModelProperty("在线支付金额")
    private BigDecimal onlineMoney;

    @ApiModelProperty("支付订单号")
    private String tradeNo;

    @ApiModelProperty("后台修改的订单金额（差价）类型")
    private String updatePriceSymbol;

    @ApiModelProperty("商品满减总额")
    private BigDecimal productReduceMoney;

    @ApiModelProperty("支付方式(10余额支付 20微信支付)文本")
    private String payTypeText;

    @ApiModelProperty("付款状态(10未付款 20已付款)文本")
    private String payStatusText;

    @ApiModelProperty("配送方式(10外卖配送 20上门取30打包带走40店内就餐")
    private String deliveryTypeText;

    @ApiModelProperty("发货状态(10未发货 20全部已发货 30部分发货)文本")
    private String deliveryStatusText;

    @ApiModelProperty("收货状态(10未收货 20已收货)文本")
    private String receiptStatusText;

    @ApiModelProperty("用餐方式0外卖1店内")
    private String orderTypeText;

    @ApiModelProperty("订单状态10=>进行中，20=>已经取消，30=>已完成文本")
    private String orderStatusText;

    @ApiModelProperty("订单商品列表")
    private List<OrderProductVo> product;

    @ApiModelProperty("订单收货地址")
    private OrderAddressVo address;

    @ApiModelProperty("订单状态文本内容")
    private String stateText;

    @ApiModelProperty("剩余付款时间文本内容")
    private String payEndTimeText;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("是否允许退款")
    private Boolean isAllowRefund;

    @ApiModelProperty("物流公司")
    private ExpressListVo express;

    @ApiModelProperty("是否显示表单")
    private Boolean showTable;

    @ApiModelProperty("微信商户号id")
    private String mchId;

    @ApiModelProperty("门店")
    private SupplierVo supplier;

}
