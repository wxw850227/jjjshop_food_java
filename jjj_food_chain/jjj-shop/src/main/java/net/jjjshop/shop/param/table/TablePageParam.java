package net.jjjshop.shop.param.table;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SupplierPageParam对象", description = "商户分页列表")
public class TablePageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("桌位编号")
    private String tableNo;

    @ApiModelProperty("区域id")
    private Integer areaId;

    @ApiModelProperty("类型id")
    private Integer typeId;
}
