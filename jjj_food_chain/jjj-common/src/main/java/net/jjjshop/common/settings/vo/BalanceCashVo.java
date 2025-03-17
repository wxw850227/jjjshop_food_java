package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("余额提现设置VO")
public class BalanceCashVo implements Serializable {
    private static final long serialVersionUID = 1L;
    // 是否开启
    private Integer isOpen;
    // 最低提现金额
    private BigDecimal minMoney;
    // 提现比例
    private BigDecimal cashRatio;
    //支付方式，10微信，20支付宝，30银行卡
    private List<Integer> payType;

    public BalanceCashVo(){
        this.isOpen = 0;
        this.minMoney = BigDecimal.ZERO;
        this.cashRatio = new BigDecimal(100);
        this.payType = new ArrayList<>();
        payType.add(10);
        payType.add(30);
    }

}
