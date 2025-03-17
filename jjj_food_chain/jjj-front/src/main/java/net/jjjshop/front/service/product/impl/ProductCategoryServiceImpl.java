package net.jjjshop.front.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.cashier.ProductCategoryMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.param.order.CategoryListParam;
import net.jjjshop.front.service.product.ProductCategoryService;
import net.jjjshop.front.service.product.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品分类表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryCache productCategoryCache;
    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 商品详情
     * @param productId
     * @param user
     * @return
     */
    public List<CategoryVo> getList(Integer productId, User user) {
        List<CategoryVo> cache = productCategoryCache.getCache();
        return cache;
    }

    @Override
    public List<CategoryVo> getList(CategoryListParam param) {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<ProductCategory>();
        //0普通1特殊
        wrapper.orderByDesc(ProductCategory::getIsSpecial);
        wrapper.orderByAsc(ProductCategory::getSort)
                .orderByDesc(ProductCategory::getCreateTime);
        if(param.getShopSupplierId() != null && param.getShopSupplierId() != 0){
            wrapper.eq(ProductCategory::getShopSupplierId,param.getShopSupplierId());
        }
        //0外卖1店内
        if(param.getType() != null){
            wrapper.eq(ProductCategory::getType,param.getType());
        }
        List<ProductCategory> list = this.list(wrapper);
        List<CategoryVo> voList = new ArrayList<>();
        for(ProductCategory category : list){
            voList.add(transVo(category,param));
        }
        return voList;
    }

    public CategoryVo transVo(ProductCategory category,CategoryListParam param){
        CategoryVo vo = new CategoryVo();
        BeanUtils.copyProperties(category,vo);
        vo.setImagePath(uploadFileUtils.getFilePath(vo.getImageId()));
        vo.setProducts(productService.getListByCategoryIds(category.getCategoryId(),param));
        return vo;
    }
}
