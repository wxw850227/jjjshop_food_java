package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("签到设置VO")
public class SignVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean isOpen;
    private Boolean isIncrease;
    private Integer increaseReward;
    private Integer noIncrease;
    private Integer everSign;
    private String content;
    private List<reward> rewardData;

    @Data
    @Accessors(chain = true)
    @ApiModel("连续签到额外奖励VO")
    public static class reward implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer day;
        private Boolean isIntegral;
        private Integer integral;
        private Boolean isCoupon;
        private List<Integer> coupon;
        private Boolean isPoint;
        private Integer point;

        public reward() {
            this.day = 7;
            this.isIntegral = true;
            this.integral = 0;
            this.isCoupon = false;
            this.coupon = new ArrayList<>();
            this.isPoint = false;
            this.point = 0;
        }
    }

    public SignVo() {
        this.isOpen = false;
        this.isIncrease = false;
        this.increaseReward = 0;
        this.noIncrease = 0;
        this.everSign = 0;
        this.content = "";
        this.rewardData = new ArrayList<>();
    }
}
