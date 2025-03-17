package net.jjjshop.front.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel("登录用户信息TokenVO")
public class UserCashListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("分销商用户id")
    private Integer userId;

    @ApiModelProperty("提现金额")
    private BigDecimal money;

    @ApiModelProperty("打款方式 (10微信 20支付宝 30银行卡)")
    private Integer payType;

    @ApiModelProperty("支付宝姓名")
    private String alipayName;

    @ApiModelProperty("支付宝账号")
    private String alipayAccount;

    @ApiModelProperty("开户行名称")
    private String bankName;

    @ApiModelProperty("银行开户名")
    private String bankAccount;

    @ApiModelProperty("银行卡号")
    private String bankCard;

    @ApiModelProperty("申请状态 (10待审核 20审核通过 30驳回 40已打款)")
    private Integer applyStatus;

    @ApiModelProperty("审核时间")
    private Date auditTime;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

    @ApiModelProperty("实际到账金额")
    private BigDecimal realMoney;

    @ApiModelProperty("提现比例")
    private BigDecimal cashRatio;

    @ApiModelProperty("打款方式 (10微信 20支付宝 30银行卡)")
    private String payTypeText;

    @ApiModelProperty("申请状态 (10待审核 20审核通过 30驳回 40已打款)")
    private String applyStatusText;
}
