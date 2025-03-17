package net.jjjshop.front.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.front.param.order.CategoryListParam;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.param.product.UserCartParam;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.front.vo.product.ProductUserCartVo;
import net.jjjshop.front.vo.product.UserCartVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户购物车 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-08-02
 */
@Repository
public interface UserCartMapper extends BaseMapper<UserCart> {
    /**
     * 购物车所有商品
     * @param userId
     * @return
     */
    List<ProductUserCartVo> getAll(Integer userId);

    List<ProductUserCartVo> getList(UserCartParam param);

    UserCartVo getCartInfo(UserCartParam param);

    List<ProductBuyVo> getCartList(OrderBuyParam orderBuyParam);

    Integer getCartNum(CategoryListParam productParam);
}
