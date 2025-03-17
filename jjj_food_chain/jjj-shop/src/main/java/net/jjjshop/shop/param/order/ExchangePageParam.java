package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderRefundPageParam对象", description = "售后单分页参数")
public class ExchangePageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    private Integer orderStatus;

    private String nickName;

}
