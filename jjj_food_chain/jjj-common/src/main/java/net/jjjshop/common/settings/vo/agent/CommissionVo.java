package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("佣金设置VO")
public class CommissionVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer firstMoney;
    private Integer secondMoney;
    private Integer thirdMoney;

    public CommissionVo() {
        this.firstMoney = 0;
        this.secondMoney = 0;
        this.thirdMoney = 0;
    }
}
