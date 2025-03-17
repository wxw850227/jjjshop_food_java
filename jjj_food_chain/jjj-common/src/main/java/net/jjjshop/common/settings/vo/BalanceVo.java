package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("余额设置VO")
public class BalanceVo implements Serializable {
    private static final long serialVersionUID = 1L;
    // 是否开启
    private Integer isOpen;
    // 是否可以自定义
    private Integer isPlan;
    // 最低充值金额
    private BigDecimal minMoney;
    // 充值说明
    private String describe;

    public BalanceVo(){
        this.isOpen = 0;
        this.isPlan = 0;
        this.minMoney = BigDecimal.ZERO;
        this.describe = "a) 账户充值仅限在线方式支付，充值金额实时到账；\n" + "b) 有问题请联系客服;\n";
    }

}
