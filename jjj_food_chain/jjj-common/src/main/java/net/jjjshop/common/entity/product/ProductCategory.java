package net.jjjshop.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品分类表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_product_category")
@ApiModel(value = "Category对象")
public class ProductCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品分类id")
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("上级分类id")
    private Integer parentId;

    @ApiModelProperty("分类图片id")
    private Integer imageId;

    @ApiModelProperty("0普通1特殊")
    private Boolean isSpecial;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("0外卖1店内")
    private Integer type;

    @ApiModelProperty("门店id")
    private Integer shopSupplierId;

    @ApiModelProperty("是否显示1显示0隐藏")
    private Boolean status;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
