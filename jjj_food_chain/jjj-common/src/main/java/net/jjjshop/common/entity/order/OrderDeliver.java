package net.jjjshop.common.entity.order;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 外卖配送订单管理
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_order_deliver")
@ApiModel(value = "OrderDeliver对象")
public class OrderDeliver implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号id")
    @TableId(value = "deliver_id", type = IdType.AUTO)
    private Integer deliverId;

    @ApiModelProperty("配送公司(10商家配送20达达30配送员)")
    private Integer deliverSource;

    @ApiModelProperty("第三方配送单号")
    private String clientId;

    @ApiModelProperty("配送距离")
    private Integer distance;

    @ApiModelProperty("配送费用")
    private BigDecimal price;

    @ApiModelProperty("骑手姓名")
    private String linkman;

    @ApiModelProperty("骑手电话")
    private String phone;

    @ApiModelProperty("配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)")
    private Integer deliverStatus;

    @ApiModelProperty("配送时间")
    private Date deliverTime;

    @ApiModelProperty("订单状态(10进行中 20被取消 30已完成)")
    private Integer status;

    @ApiModelProperty("订单号")
    private Integer orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("门店id")
    private Integer shopSupplierId;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
