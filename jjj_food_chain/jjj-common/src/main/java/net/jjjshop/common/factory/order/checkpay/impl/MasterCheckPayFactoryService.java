package net.jjjshop.common.factory.order.checkpay.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.enums.DeductStockTypeEnum;
import net.jjjshop.common.factory.order.checkpay.CheckPayFactoryService;
import net.jjjshop.common.service.product.ProductService;
import net.jjjshop.common.service.product.ProductSkuService;
import net.jjjshop.framework.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 主订单工厂类
 */
@Slf4j
@Service
public class MasterCheckPayFactoryService implements CheckPayFactoryService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;

    /**
     * 判断订单是否允许付款
     */
    public void checkOrderStatus(List<OrderProduct> productList){
        for (OrderProduct orderProduct:productList) {
            Product product = productService.getById(orderProduct.getProductId());
            // 判断商品是否下架
            if (product.getProductStatus() != 10) {
                throw new BusinessException(String.format("很抱歉，商品 [%s] 已下架", product.getProductName()));
            }
            // 获取商品的sku信息
            ProductSku productSku = productSkuService.getOne(new LambdaQueryWrapper<ProductSku>()
                    .eq(ProductSku::getProductId, orderProduct.getProductId())
                    .eq(ProductSku::getProductSkuId, orderProduct.getProductSkuId()));
            // sku已不存在
            if (productSku == null) {
                throw new BusinessException(String.format("很抱歉，商品 [%s] sku已不存在，请重新下单", product.getProductName()));
            }
            // 付款减库存
            if(orderProduct.getDeductStockType() == DeductStockTypeEnum.PAYMENT.getValue()
                && orderProduct.getTotalNum() > productSku.getStockNum()){
                throw new BusinessException(String.format("很抱歉，商品 [%s] 库存不足", product.getProductName()));
            }
        }
    }
}
