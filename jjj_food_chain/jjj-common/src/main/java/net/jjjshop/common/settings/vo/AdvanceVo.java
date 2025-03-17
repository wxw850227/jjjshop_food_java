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
@ApiModel("预售活动设置")
public class AdvanceVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //未支付定金订单自动关闭时间（分钟）,设置0则不自动关闭
    private Double endTime;
    //未支付尾款订单自动关闭时间（小时）,设置0则不自动关闭
    private Double payTime;
    // 是否开启分销
    private Boolean isAgent;
    //是否允许退定金
    private Boolean moneyReturn;
    //活动图片
    private List<String> image;

    public AdvanceVo(){
        this.endTime = 10.0;
        this.payTime = 20.0;
        this.moneyReturn = true;
        this.isAgent = false;
        this.image = new ArrayList<>();
        this.image.add("https://qn-cdn.jjjshop.net/20220613154036c46196044.jpg");
    }

}
