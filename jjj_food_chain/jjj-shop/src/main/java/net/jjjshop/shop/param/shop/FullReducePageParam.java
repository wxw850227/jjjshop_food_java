package net.jjjshop.shop.param.shop;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户操作日志分页对象", description = "用户操作日志分页对象")
public class FullReducePageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
