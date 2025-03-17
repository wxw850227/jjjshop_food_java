package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("秒杀设置VO")
public class SeckillVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 是否开启积分
    private Boolean isPoint;
    // 是否开启分销
    private Boolean isAgent;
    //未付款订单自动关闭时间,分钟
    private Integer orderClose;

    public SeckillVo() {
        this.orderClose = 10;
        this.isPoint = false;
        this.isAgent = false;
    }
}
