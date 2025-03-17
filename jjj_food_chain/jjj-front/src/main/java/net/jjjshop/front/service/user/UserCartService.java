package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.product.CartParam;
import net.jjjshop.front.param.product.UserCartParam;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.front.vo.product.ProductUserCartVo;
import net.jjjshop.front.vo.product.UserCartVo;

import java.util.List;

/**
 * 用户购物车 服务类
 * @author jjjshop
 * @since 2022-08-02
 */
public interface UserCartService extends BaseService<UserCart> {

    /**
     * 获取购物车所有商品
     * @param user
     * @return
     */
    List<ProductUserCartVo> getAll(User user, UserCartParam param);

    /**
     * 添加商品
     * @param user
     * @param param
     * @return
     */
    Boolean add(User user, UserCartParam param);

    /**
     * 移除商品
     * @return
     */
    Boolean sub(UserCartParam param);

    /**
     * 删除商品
     * @return
     */
    Boolean delete(UserCartParam param);

    /**
     * 购物车商品数量
     * @param user
     * @return
     */
    Integer getTotalCartNum(User user);

    /**
     * 详情
     * @return
     */
    UserCart detail(UserCartParam param);

    /**
     * 下单后，购物车移除
     * @param cartIds
     */
    void clearAll(String cartIds);

    UserCartVo getCartInfo(UserCartParam param);

    List<ProductBuyVo> getCartList(OrderBuyParam orderBuyParam);

    //编辑分类列表商品数量
    boolean productSub(UserCartParam param);
}
