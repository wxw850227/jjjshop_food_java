

package net.jjjshop.common.vo.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("微信小程序发货录入Params")
public class WxExpressParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单，需要上传物流信息的订单")
    private JSONObject order_key;

    @ApiModelProperty("物流模式，发货方式枚举值：1、实体物流配送采用快递公司进行实体物流配送形式 2、同城配送 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式 4、用户自提")
    private Integer logistics_type;

    @ApiModelProperty("发货模式，发货模式枚举值：1、UNIFIED_DELIVERY（统一发货）2、SPLIT_DELIVERY（分拆发货） 示例值: UNIFIED_DELIVERY")
    private Integer delivery_mode;

    @ApiModelProperty("分拆发货模式时必填，用于标识分拆发货模式下是否已全部发货完成，只有全部发货完成的情况下才会向用户推送发货完成通知。示例值: true/false")
    private Boolean is_all_delivered;

    @ApiModelProperty("物流信息列表，发货物流单列表，支持统一发货（单个物流单）和分拆发货（多个物流单）两种模式，多重性: [1, 10]")
    private JSONArray shipping_list;

    @ApiModelProperty("上传时间，用于标识请求的先后顺序 示例值: `2022-12-15T13:29:35.120+08:00`")
    private String upload_time;

    @ApiModelProperty("支付者，支付者信息")
    private JSONObject payer;

}
