package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单商品导出VO")
public class OrderProductExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品规格信息")
    private String productAttr;

    @ApiModelProperty("购买数量")
    private Integer totalNum;

    @ApiModelProperty("商品总价(数量×单价)")
    private BigDecimal totalPrice;
}
