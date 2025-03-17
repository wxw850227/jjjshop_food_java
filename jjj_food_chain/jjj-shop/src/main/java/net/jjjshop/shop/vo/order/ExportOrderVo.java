package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.util.excel.ExcelExport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("外卖订单导出VO")
public class ExportOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    @ExcelExport("订单号")
    private String orderNo;

    @ApiModelProperty("商品信息")
    @ExcelExport("商品信息")
    private String product;

    @ApiModelProperty("订单总额")
    @ExcelExport("订单总额")
    private BigDecimal totalPrice;

    @ApiModelProperty("包装费")
    @ExcelExport("包装费")
    private BigDecimal bagPrice;

    @ApiModelProperty("优惠券抵扣金额")
    @ExcelExport("优惠券抵扣金额")
    private BigDecimal couponMoney;

    @ApiModelProperty("实际付款金额(包含运费)")
    @ExcelExport("实际付款金额")
    private BigDecimal payPrice;

    @ApiModelProperty("支付方式")
    @ExcelExport("支付方式")
    private String payTypeText;

    @ApiModelProperty("买家留言")
    @ExcelExport("买家留言")
    private String buyerRemark;

    @ApiModelProperty("付款时间")
    @ExcelExport("付款时间")
    private Date payTime;

    @ApiModelProperty("发货时间")
    @ExcelExport("发货时间")
    private Date deliveryTime;

    @ApiModelProperty("收货时间")
    @ExcelExport("收货时间")
    private Date receiptTime;

    @ApiModelProperty("创建时间")
    @ExcelExport("创建时间")
    private Date createTime;

    @ApiModelProperty("购买用户")
    @ExcelExport("购买用户")
    private String nickName;

    @ApiModelProperty("订单状态")
    @ExcelExport("订单状态")
    private String orderStatusText;

    @ApiModelProperty("配送方式")
    @ExcelExport("配送方式")
    private String deliveryTypeText;

    @ApiModelProperty("支付状态文本")
    @ExcelExport("支付状态文本")
    private String payStatusText;

    @ApiModelProperty("发货状态文本")
    @ExcelExport("发货状态文本")
    private String deliveryStatusText;

    @ApiModelProperty("详细地址")
    @ExcelExport("详细地址")
    private String detailRegion;

    @ApiModelProperty("收货状态(10未收货 20已收货)文本")
    @ExcelExport("收货状态")
    private String receiptStatusText;

    @ApiModelProperty("收货人姓名")
    @ExcelExport("收货人姓名")
    private String addressName;

    @ApiModelProperty("收货人电话")
    @ExcelExport("收货人电话")
    private String addressPhone;

    @ApiModelProperty("联系人")
    @ExcelExport("联系人")
    private String extractLinkman;

    @ApiModelProperty("联系电话")
    @ExcelExport("联系电话")
    private String extractPhone;

}
