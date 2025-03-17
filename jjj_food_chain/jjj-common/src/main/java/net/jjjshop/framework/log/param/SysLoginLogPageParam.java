

package net.jjjshop.framework.log.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 系统登录日志 分页参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统登录日志分页参数")
public class SysLoginLogPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
