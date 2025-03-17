package net.jjjshop.common.entity.settings;

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
 * 小票打印机记录表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_printer")
@ApiModel(value = "Printer对象")
public class Printer implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("打印机id")
    @TableId(value = "printer_id", type = IdType.AUTO)
    private Integer printerId;

    @ApiModelProperty("打印机名称")
    private String printerName;

    @ApiModelProperty("打印机类型")
    private String printerType;

    @ApiModelProperty("打印机配置")
    private String printerConfig;

    @ApiModelProperty("打印联数(次数)")
    private Integer printTimes;

    @ApiModelProperty("排序 (数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("是否删除0=显示1=隐藏")
    private Integer isDelete;

    @ApiModelProperty("商户id")
    private Integer shopSupplierId;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
