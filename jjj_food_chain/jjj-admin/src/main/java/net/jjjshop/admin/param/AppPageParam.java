

package net.jjjshop.admin.param;

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
@ApiModel(value = "AppPageParam对象", description = "app查询参数")
public class AppPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
