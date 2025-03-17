package net.jjjshop.shop.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公共分页查询参数", description = "没有参数的分页")
public class CommonPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
