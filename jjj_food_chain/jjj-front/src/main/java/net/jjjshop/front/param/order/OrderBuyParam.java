package net.jjjshop.front.param.order;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bouncycastle.LICENSE;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 订单参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "订单参数对象", description = "订单参数对象")
public class OrderBuyParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("就餐类型,10外卖配送,20外卖自提,30店内打包40店内堂食")
    @NotNull(message = "就餐类型不能为空")
    private Integer delivery;

    @ApiModelProperty("自提店id")
    @NotNull(message = "门店id不能为空")
    private Integer storeId;

    @ApiModelProperty("送餐时间")
    private String mealtime;

    @ApiModelProperty("支付渠道来源")
    @NotBlank(message = "支付渠道来源不能为空")
    private String paySource;

    @ApiModelProperty("tableId")
    private Integer tableId;

    @ApiModelProperty("购物车")
    @NotBlank(message = "购物车ID不能为空")
    private String cartIds;

    @ApiModelProperty("店铺id")
    @NotNull(message = "店铺id不能为空")
    private Integer shopSupplierId;

    @ApiModelProperty("用餐方式0外卖1店内")
    @NotNull(message = "orderType不能为空")
    private Integer orderType;

    private List<Integer> cartIdList;

    @ApiModelProperty("appId")
    private Integer appId;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("自提电话")
    private String phone;

    @ApiModelProperty("自提联系人")
    private String linkman;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("购买商品数量")
    private Integer productNum;

    @ApiModelProperty("商品skuId")
    private Integer productSkuId;

    private Integer userId;
}
