package net.jjjshop.front.vo.supplier;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.supplier.Supplier;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商户Vo")
public class SupplierVo extends Supplier {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("logo图片路径")
    private String businessFilePath;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("地址坐标")
    private String coordinate;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("地区")
    private String region;

    @ApiModelProperty("店内营业时间")
    private List<String> storeTimeList;

    @ApiModelProperty("自提营业时间")
    private List<String> pickTimeList;

    @ApiModelProperty("外卖营业时间")
    private List<String> deliveryTimeList;

    @ApiModelProperty("外卖配送方式")
    private List<Integer> deliverySetList;

    @ApiModelProperty("店内用餐方式")
    private List<Integer> storeSetList;

    @ApiModelProperty("门店距离")
    private BigDecimal distanceValue;

    @ApiModelProperty("门店距离字符串")
    private String distance;

    @ApiModelProperty("门店距离单位")
    private String distanceUnit;

    @ApiModelProperty("是否可选")
    private Integer isSelect;
}
