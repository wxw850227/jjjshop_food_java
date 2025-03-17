package net.jjjshop.common.vo.store.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.ProductAttribute;
import net.jjjshop.common.entity.table.TableType;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品属性VO")
public class TableTypeVo extends TableType {

    @ApiModelProperty("属性值List")
    private List<String> attributeValueList;

}
