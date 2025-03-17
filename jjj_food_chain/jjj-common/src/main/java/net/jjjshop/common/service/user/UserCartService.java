package net.jjjshop.common.service.user;

import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 用户购物车 服务类
 * @author jjjshop
 * @since 2022-08-02
 */
public interface UserCartService extends BaseService<UserCart> {

    void updaterCart(ProductSku sku);
}
