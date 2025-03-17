package net.jjjshop.shop.param.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "运费模板", description = "运费模板新增修改参数")
public class DeliveryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("模板id")
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Integer deliveryId;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("计费方式(10按件数 20按重量)")
    private Integer method;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @NotNull(message = "运费规则不能为空")
    @ApiModelProperty("运费规则)")
    private List<RuleParam> rule;

    @Data
    @Accessors(chain = true)
    @ApiModel("运费规则")
    public static class RuleParam implements Serializable{
        private static final long serialVersionUID = 1L;
        private String region;
        private Double first;
        private BigDecimal firstFee;
        private Double additional;
        private BigDecimal additionalFee;
        public RuleParam(){

        }
    }
}
