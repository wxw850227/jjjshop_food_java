package net.jjjshop.shop.vo.setting;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("打印机VO")
public class PrinterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("打印机id")
    private Integer printerId;

    @ApiModelProperty("打印机名称")
    private String printerName;

    @ApiModelProperty("打印机类型")
    private String printerType;

    @ApiModelProperty("打印机配置")
    private JSONObject printerConfig;

    @ApiModelProperty("打印联数(次数)")
    private Integer printTimes;

    @ApiModelProperty("排序 (数字越小越靠前)")
    private Integer sort;


}
