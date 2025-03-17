package net.jjjshop.shop.param.setting;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "小票打印机查询参数", description = "小票打印机查询参数")
public class PrinterParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("打印机id")
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

    @ApiModelProperty("飞鹅云设置")
    private JSONObject feieYun;

    @ApiModelProperty("365设置")
    private JSONObject printCenter;

}
