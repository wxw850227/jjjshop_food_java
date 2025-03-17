package net.jjjshop.common.factory.product.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.enums.DeductStockTypeEnum;
import net.jjjshop.common.factory.product.ProductFactoryService;
import net.jjjshop.common.factory.product.vo.UpdateProductStockVo;
import net.jjjshop.common.service.product.ProductService;
import net.jjjshop.common.service.product.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 主订单工厂类
 */
@Slf4j
@Service
public class MasterProductFactoryService implements ProductFactoryService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;

    /**
     * 更新商品库存 (针对下单减库存的商品)
     */
    public void updateProductStock(List<UpdateProductStockVo> productList){
        for(UpdateProductStockVo vo:productList){
            // 下单减库存
            if (vo.getDeductStockType().intValue() == DeductStockTypeEnum.CREATE.getValue()) {
                this.decStock(vo);
            }
        }
    }

    /**
     * 更新商品库存销量（订单付款后）
     */
    public void updateProductStockSales(List<UpdateProductStockVo> productList, int shopSupplierId){
        for(UpdateProductStockVo vo:productList) {
            // 累计销量
            productService.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, vo.getProductId())
                    .setSql("`sales_actual` = `sales_actual` + " + vo.getTotalNum()));
            // 付款减库存
            if (vo.getDeductStockType().intValue() == DeductStockTypeEnum.PAYMENT.getValue()) {
                this.decStock(vo);
            }
        }
    }

    /**
     * 减少库存
     * @param vo
     */
    private void decStock(UpdateProductStockVo vo){
        // 更新商品主表库存
        productService.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, vo.getProductId())
                .setSql("`product_stock` = `product_stock` - " + vo.getTotalNum()));
        // 更新规格库存
        productSkuService.update(new LambdaUpdateWrapper<ProductSku>().eq(ProductSku::getProductId, vo.getProductId())
                .eq(ProductSku::getSpecSkuId, vo.getSpecSkuId())
                .setSql("`stock_num` = `stock_num` - " + vo.getTotalNum()));
    }

    /**
     * 回退商品库存
     */
    public void backProductStock(List<UpdateProductStockVo> productList, boolean isPayOrder){
        for(UpdateProductStockVo vo:productList) {
            // 已支付
            if (isPayOrder) {
                this.addStock(vo);
            }else{
                if (vo.getDeductStockType().intValue() == DeductStockTypeEnum.CREATE.getValue()) {
                    this.addStock(vo);
                }
            }
        }
    }

    /**
     * 增加库存
     * @param vo
     */
    private void addStock(UpdateProductStockVo vo){
        // 更新商品主表库存
        productService.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, vo.getProductId())
                .setSql("`product_stock` = `product_stock` + " + vo.getTotalNum()));
        // 更新规格库存
        productSkuService.update(new LambdaUpdateWrapper<ProductSku>().eq(ProductSku::getProductId, vo.getProductId())
                .eq(ProductSku::getSpecSkuId, vo.getSpecSkuId())
                .setSql("`stock_num` = `stock_num` + " + vo.getTotalNum()));
    }
}
