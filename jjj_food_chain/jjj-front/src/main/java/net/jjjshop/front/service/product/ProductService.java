package net.jjjshop.front.service.product;

import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.order.CategoryListParam;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.common.vo.product.ProductListVo;
import net.jjjshop.front.vo.product.ProductVo;

import java.util.List;
import java.util.Map;

/**
 * 商品记录表 服务类
 * @author jjjshop
 * @since 2022-07-01
 */
public interface ProductService extends BaseService<Product> {

    /**
     * 商品详情
     * @param product
     * @param user
     * @return
     */
    ProductVo getDetail(Product product, User user);

    /**
     * 购买商品信息,兼容购物车，返回list
     * @param masterOrderBuyParam
     * @return
     */
    List<ProductBuyVo> getOrderProductListByNow(OrderBuyParam masterOrderBuyParam);

    /**
     * 商品查询
     * @param productParam
     * @param user
     * @return
     */
    Paging<ProductListVo> getList(ProductParam productParam, User user);

    /**
     * 商品推荐
     * @param location
     * @param user
     * @return
     */
    Map<String, Object> getRecommend(Integer location, User user);

    /**
     * 按商品id查询
     * @param productIds
     * @param user
     * @return
     */
    List<ProductListVo> getListByProductIds(List<Integer> productIds, User user);

    /**
     * 按商品分类查询
     * @param categoryId
     * @return
     */
    List<ProductListVo> getListByCategoryIds(Integer categoryId, CategoryListParam param);

    /**
     * 购物车商品
     * @param user
     * @param orderBuyParam
     * @return
     */
    List<ProductBuyVo> getOrderProductListByCart(User user, OrderBuyParam orderBuyParam);

    // 优惠信息
    Map<String, Object>  getDiscount(Integer productId);
}
