package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.service.product.ProductService;
import net.jjjshop.common.vo.product.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductDataUtils {

    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 获取商品浏览榜单
     * @param
     * @return
     */
    public List<ProductVo> getViewsRanking(Integer shopSupplierId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(Product::getViewTimes, 0);
        wrapper.orderByDesc(Product::getViewTimes);
        if(shopSupplierId != null && shopSupplierId>0) {
            wrapper.eq(Product::getShopSupplierId, shopSupplierId);
        }
        wrapper.last("LIMIT 0,10");
        List<Product> list = productService.list(wrapper);
        List<ProductVo> result = list.stream().map(e -> {
            ProductVo vo = new ProductVo();
            BeanUtils.copyProperties(e, vo);
            vo.setImagePath(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取商品总数
     * @param
     * @return
     */
    public Integer getProductTotal(Integer shopSupplierId, String type) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getIsDelete, 0);
        if(shopSupplierId != null && shopSupplierId>0) {
            wrapper.eq(Product::getShopSupplierId, shopSupplierId);
        }
        return productService.count(wrapper);
    }

    /**
     * 获取商品库存总数
     * @param
     * @return
     */
    public Integer getProductStockTotal(Integer shopSupplierId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getIsDelete, 0);
        wrapper.lt(Product::getProductStock, 20);
        if(shopSupplierId != null && shopSupplierId>0) {
            wrapper.eq(Product::getShopSupplierId, shopSupplierId);
        }
        return productService.count(wrapper);
    }

}
