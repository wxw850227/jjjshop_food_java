

package net.jjjshop.shop.param.shopUser;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ShopUser分页查询对象", description = "ShopUser分页查询对象")
public class ShopUserPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
