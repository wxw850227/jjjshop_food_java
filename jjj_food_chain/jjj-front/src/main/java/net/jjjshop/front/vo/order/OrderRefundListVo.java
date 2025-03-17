package net.jjjshop.front.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel("售后订单列表VO")
public class OrderRefundListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("售后单id")
    private Integer orderRefundId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单商品id")
    private Integer orderProductId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("售后类型(10退货退款 20换货 30退款)")
    private Integer type;

    @ApiModelProperty("用户申请原因(说明)")
    private String applyDesc;

    @ApiModelProperty("商家审核状态(0待审核 10已同意 20已拒绝)")
    private Integer isAgree;

    @ApiModelProperty("商家拒绝原因(说明)")
    private String refuseDesc;

    @ApiModelProperty("实际退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("用户是否发货(0未发货 1已发货)")
    private Integer isUserSend;

    @ApiModelProperty("用户发货时间")
    private Integer sendTime;

    @ApiModelProperty("用户发货物流公司id")
    private String expressId;

    @ApiModelProperty("用户发货物流单号")
    private String expressNo;

    @ApiModelProperty("商家收货状态(0未收货 1已收货)")
    private Integer isReceipt;

    @ApiModelProperty("售后单状态(0进行中 10已拒绝 20已完成 30已取消)")
    private Integer status;

    @ApiModelProperty("平台发货时间")
    private Integer deliverTime;

    @ApiModelProperty("平台发货物流公司id")
    private String sendExpressId;

    @ApiModelProperty("平台发货物流单号")
    private String sendExpressNo;

    @ApiModelProperty("平台是否发货(0未发货 1已发货)")
    private Integer isPlateSend;

    @ApiModelProperty("售后订单货物信息")
    private OrderProductVo orderProduct;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("售后单状态(0进行中 10已拒绝 20已完成 30已取消)")
    private String stateText;

}
