package net.jjjshop.shop.service.statistics;

import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.vo.product.ProductVo;
import net.jjjshop.shop.vo.statistics.ProductSaleRankingVo;

import java.util.List;

/**
 * 商品统计数据 服务类
 * @author jjjshop
 * @since 2022-06-28
 */

public interface ProductRankingService {

    /**
     * 获取商品销售榜单
     * @param
     * @return
     */
    List<ProductSaleRankingVo> getSaleRanking();

    /**
     * 获取商品浏览榜单
     * @param
     * @return
     */
    List<ProductVo> getViewsRanking();

    /**
     * 获取商品总数
     * @param
     * @return
     */
    Integer getProductTotal();

    /**
     * 获取商品库存总数
     * @param
     * @return
     */
    Integer getProductStockTotal(Integer productType);

    List<ProductSaleRankingVo> getSaleNumRank(OrderPageParam param);

    List<ProductSaleRankingVo> getSaleMoneyRank(OrderPageParam param);
}
