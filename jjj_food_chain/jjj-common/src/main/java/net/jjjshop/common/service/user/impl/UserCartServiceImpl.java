package net.jjjshop.common.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.mapper.user.UserCartMapper;
import net.jjjshop.common.service.user.UserCartService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户购物车 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */
@Slf4j
@Service
public class UserCartServiceImpl extends BaseServiceImpl<UserCartMapper, UserCart> implements UserCartService {

    //更新购物车商品价格
    @Override
    public void updaterCart(ProductSku sku) {
        List<UserCart> cartList = this.list(new LambdaQueryWrapper<UserCart>()
                .eq(UserCart::getProductSkuId,sku.getProductSkuId()));
        for(UserCart cart : cartList){
            cart.setPrice(sku.getProductPrice());
            cart.setProductPrice(sku.getProductPrice());
            cart.setBagPrice(sku.getBagPrice());
            this.updateById(cart);
        }
    }
}
