package net.jjjshop.common.entity.product;

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
 * 商品规格组记录表
 *
 * @author jjjfood
 * @since 2023-12-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_spec")
@ApiModel(value = "Spec对象")
public class Spec implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格组id")
    @TableId(value = "spec_id", type = IdType.AUTO)
    private Integer specId;

    @ApiModelProperty("规格组名称")
    private String specName;

    @ApiModelProperty("门店id")
    private Integer shopSupplierId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
