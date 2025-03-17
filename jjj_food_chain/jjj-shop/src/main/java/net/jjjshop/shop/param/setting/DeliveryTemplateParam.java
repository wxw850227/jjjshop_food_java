package net.jjjshop.shop.param.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "物流公司查询参数", description = "物流公司查询参数")
public class DeliveryTemplateParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("电子面单模板id")
    private Integer templateId;

    @ApiModelProperty("模板名称")
    private String templateNum;

    @ApiModelProperty("电子面单模板编码")
    private String templateName;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;
}
