package net.jjjshop.common.entity.table;

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
 * 桌位管理
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_table")
@ApiModel(value = "Table对象")
public class Table implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "table_id", type = IdType.AUTO)
    private Integer tableId;

    @ApiModelProperty("桌位编号")
    private String tableNo;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("区域id")
    private Integer areaId;

    @ApiModelProperty("类型id")
    private Integer typeId;

    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("类型名称")
    private String typeName;

    @ApiModelProperty("门店id")
    private Integer shopSupplierId;

    @ApiModelProperty("最小人数")
    private Integer minNum;

    @ApiModelProperty("最大人数")
    private Integer maxNum;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
