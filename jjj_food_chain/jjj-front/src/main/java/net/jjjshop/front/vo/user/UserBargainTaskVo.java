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
public class UserBargainTaskVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("砍价任务id")
    private Integer bargainTaskId;

    @ApiModelProperty("用户id(发起人)")
    private Integer userId;

    @ApiModelProperty("砍价活动id")
    private Integer bargainActivityId;

    @ApiModelProperty("商品id")
    private Integer bargainProductId;

    @ApiModelProperty("商品sku标识")
    private Integer bargainProductSkuId;

    @ApiModelProperty("商品规格id")
    private Integer productSkuId;

    @ApiModelProperty("商品原价")
    private BigDecimal productPrice;

    @ApiModelProperty("砍价底价")
    private BigDecimal bargainPrice;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品封面图片id")
    private Integer imageId;

    @ApiModelProperty("商品规格")
    private String productAttr;

    @ApiModelProperty("帮砍人数")
    private Integer peoples;

    @ApiModelProperty("已砍人数")
    private Integer cutPeople;

    @ApiModelProperty("已砍金额")
    private BigDecimal cutMoney;

    @ApiModelProperty("实际购买金额")
    private BigDecimal actualPrice;

    @ApiModelProperty("是否已砍到底价(0否 1是)")
    private Integer isFloor;

    @ApiModelProperty("任务截止时间")
    private Long endTime;

    @ApiModelProperty("是否购买(0未购买 1已购买)")
    private Integer isBuy;

    @ApiModelProperty("任务状态 (0砍价中 1砍价成功 2砍价失败)")
    private Integer status;

    @ApiModelProperty("图片")
    private String filePath;

    @ApiModelProperty("砍价进度")
    private BigDecimal bargainRate;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
