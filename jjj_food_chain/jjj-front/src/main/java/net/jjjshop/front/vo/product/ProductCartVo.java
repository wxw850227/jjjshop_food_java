

package net.jjjshop.front.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.UserCart;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("购物车商品详情VO")
public class ProductCartVo extends UserCart {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片")
    private String productImage;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品名称")
    private BigDecimal productPrice;

    @ApiModelProperty("规格属性")
    private String productAttr;

    @ApiModelProperty("库存")
    private Integer stockNum;

    @ApiModelProperty("规格id")
    private String specSkuId;

    @ApiModelProperty("规格类型")
    private Integer specType;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("产品状态(10上架 20仓库中 30回收站)")
    private Integer productStatus;

    @ApiModelProperty("是否会员折扣价")
    private Boolean isUserGrade;

    @ApiModelProperty("是否允许等级折扣")
    private Integer isEnableGrade;

    @ApiModelProperty("单独设置折扣的配置")
    private String aloneGradeEquity;

    @ApiModelProperty("会员折扣设置(0默认等级折扣 1单独设置折扣)")
    private Integer isAloneGrade;

    @ApiModelProperty("折扣金额类型(10百分比 20固定金额)")
    private Integer aloneGradeType;
}
