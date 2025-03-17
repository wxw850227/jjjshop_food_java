package net.jjjshop.shop.param.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "SupplierParam对象", description = "商户参数对象")
public class SupplierSettingParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @NotNull(message = "Id不能为空")
    private Integer shopSupplierId;

    @ApiModelProperty("店内营业时间")
    @NotNull(message = "店内营业时间不能为空")
    private List<String> storeTimeList;

    @ApiModelProperty("自提营业时间")
    @NotNull(message = "自提营业时间不能为空")
    private List<String> pickTimeList;

    @ApiModelProperty("外卖营业时间")
    @NotNull(message = "外卖营业时间不能为空")
    private List<String> deliveryTimeList;

    @ApiModelProperty("外卖配送方式")
    @NotNull(message = "外卖配送方式不能为空")
    private Integer[] deliverySetList;

    @ApiModelProperty("店内用餐方式")
    @NotNull(message = "店内用餐方式不能为空")
    private Integer[] storeSetList;

    @ApiModelProperty("外卖包装费类型0按商品收费1按单收费")
    @NotNull(message = "外卖包装费类型不能为空")
    private Integer bagType;

    @ApiModelProperty("店内包装费类型0按商品收费1按单收费")
    @NotNull(message = "店内包装费类型不能为空")
    private Integer storebagType;

    @ApiModelProperty("外卖包装费")
    private BigDecimal bagPrice;

    @ApiModelProperty("店内包装费")
    private BigDecimal storebagPrice;

    @ApiModelProperty("配送范围km")
    @NotNull(message = "配送范围不能为空")
    private Float deliveryDistance;

    @ApiModelProperty("最低消费")
    @NotNull(message = "最低消费不能为空")
    private BigDecimal minMoney;
}
