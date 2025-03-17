package net.jjjshop.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品记录表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_product")
@ApiModel(value = "Product对象")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("产品id")
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    @NotBlank(message = "产品名称不能为空")
    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品一口价")
    private BigDecimal productPrice;

    @ApiModelProperty("划线价")
    private BigDecimal linePrice;

    @ApiModelProperty("产品编码")
    private String productNo;

    @ApiModelProperty("产品总库存")
    private Integer productStock;

    @ApiModelProperty("商品卖点")
    private String sellingPoint;

    @ApiModelProperty("产品分类id")
    private Integer categoryId;

    @ApiModelProperty("产品规格(10单规格 20多规格)")
    private Integer specType;

    @ApiModelProperty("库存计算方式(10下单减库存 20付款减库存)")
    private Integer deductStockType;

    @NotBlank(message = "产品详情不能为空")
    @ApiModelProperty("产品详情")
    private String content;

    @ApiModelProperty("初始销量")
    private Integer salesInitial;

    @ApiModelProperty("实际销量")
    private Integer salesActual;

    @ApiModelProperty("产品排序(数字越小越靠前)")
    private Integer productSort;

    @ApiModelProperty("是否开启积分赠送(1开启 0关闭)")
    private Integer isPointsGift;

    @ApiModelProperty("是否允许使用积分抵扣(1允许 0不允许)")
    private Integer isPointsDiscount;

    @ApiModelProperty("最大积分抵扣数量")
    private Integer maxPointsDiscount;

    @ApiModelProperty("是否开启会员折扣(1开启 0关闭)")
    private Integer isEnableGrade;

    @ApiModelProperty("会员折扣设置(0默认等级折扣 1单独设置折扣)")
    private Integer isAloneGrade;

    @ApiModelProperty("单独设置折扣的配置")
    private String aloneGradeEquity;

    @ApiModelProperty("折扣金额类型(10百分比 20固定金额)")
    private Integer aloneGradeType;

    @ApiModelProperty("是否参加分销0否1是")
    private Integer isAgent;

    @ApiModelProperty("是否开启单独分销(0关闭 1开启)")
    private Integer isIndAgent;

    @ApiModelProperty("分销佣金类型(10百分比 20固定金额)")
    private Integer agentMoneyType;

    @ApiModelProperty("分销佣金(一级)")
    private BigDecimal firstMoney;

    @ApiModelProperty("分销佣金(二级)")
    private BigDecimal secondMoney;

    @ApiModelProperty("分销佣金(三级)")
    private BigDecimal thirdMoney;

    @ApiModelProperty("产品状态(10上架 20下架 )")
    private Integer productStatus;

    @ApiModelProperty("访问次数")
    private Integer viewTimes;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("供应商价格")
    private BigDecimal supplierPrice;

    @ApiModelProperty("限购数量0为不限")
    private Integer limitNum;

    @ApiModelProperty("可购买会员等级id，逗号隔开")
    private String gradeIds;

    @ApiModelProperty("0外卖1店内")
    private Integer productType;

    @ApiModelProperty("商品单位")
    private String productUnit;

    @ApiModelProperty("商品属性")
    private String productAttr;

    @ApiModelProperty("售卖加料")
    private String productFeed;

    @ApiModelProperty("特殊分类id")
    private Integer specialId;

    @ApiModelProperty("成本价")
    private BigDecimal costPrice;

    @ApiModelProperty("最小购买量")
    private Integer minBuy;

    @ApiModelProperty("包装费")
    private BigDecimal bagPrice;

    @ApiModelProperty("打印标签id")
    private Integer labelId;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
