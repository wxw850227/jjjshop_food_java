package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("分销商结算设置VO")
public class SettlementVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Integer> payType;
    private Integer wechatPayAuto;
    private BigDecimal minMoney;
    private Integer settleDays;

    public SettlementVo() {
        this.payType = new ArrayList<>();
        this.wechatPayAuto = 0;
        this.minMoney = new BigDecimal(10).setScale(2, RoundingMode.DOWN);
        this.settleDays = 10;
    }
}
