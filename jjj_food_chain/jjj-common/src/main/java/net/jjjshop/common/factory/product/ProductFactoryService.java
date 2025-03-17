package net.jjjshop.common.factory.product;

import net.jjjshop.common.factory.product.vo.UpdateProductStockVo;

import java.util.List;

/**
 * 商品工厂接口类
 */
public interface ProductFactoryService {
    /**
     * 更新商品库存 (针对下单减库存的商品)
     */
    void updateProductStock(List<UpdateProductStockVo> productList);

    /**
     * 更新商品库存销量（订单付款后）
     */
    void updateProductStockSales(List<UpdateProductStockVo> productList, int shopSupplierId);

    /**
     * 回退商品库存
     */
    void backProductStock(List<UpdateProductStockVo> productList, boolean isPayOrder);
}
