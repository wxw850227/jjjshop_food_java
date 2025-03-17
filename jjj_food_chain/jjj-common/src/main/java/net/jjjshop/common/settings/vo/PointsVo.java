package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("积分设置VO")
public class PointsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pointsName;
    private String describe;
    private Integer registerPoints;
    private Boolean isShoppingGift;
    private Integer giftRatio;
    private Boolean isShoppingDiscount;
    private Boolean isTransBalance;
    private Discount discount;


    @Data
    @Accessors(chain = true)
    @ApiModel("积分抵扣Vo")
    public static class Discount implements Serializable {
        private static final long serialVersionUID = 1L;
        private Double discountRatio;
        private BigDecimal fullOrderPrice;
        private Integer maxMoneyRatio;

        public Discount() {
            this.discountRatio = 0.00;
            this.fullOrderPrice = BigDecimal.ZERO;
            this.maxMoneyRatio = 0;
        }
    }

    public PointsVo() {
        this.pointsName = "这是一个名字";
        this.describe = "a) 积分不可兑现、不可转让,仅可在本平台使用;\n" +
                "b) 您在本平台参加特定活动也可使用积分,详细使用规则以具体活动时的规则为准;\n" +
                "c) 积分的数值精确到个位(小数点后全部舍弃,不进行四舍五入)\n" +
                "d) 买家在完成该笔交易(订单状态为“已签收”)后才能得到此笔交易的相应积分,如购买商品参加店铺其他优惠,则优惠的金额部分不享受积分获取;";
        this.registerPoints = 0;
        this.isShoppingGift = false;
        this.giftRatio = 10;
        this.isShoppingDiscount = false;
        this.isTransBalance = false;
        this.discount = new Discount();
    }
}
