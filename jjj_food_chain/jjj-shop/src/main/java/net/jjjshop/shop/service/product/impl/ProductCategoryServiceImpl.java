package net.jjjshop.shop.service.product.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.product.ProductAttribute;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.mapper.cashier.ProductCategoryMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.product.CategoryParam;
import net.jjjshop.shop.service.product.ProductCategoryService;
import net.jjjshop.shop.service.product.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品分类表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryCache productCategoryCache;
    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 新增
     * @param categoryParam
     * @return
     */
    public Boolean add(CategoryParam categoryParam){
        ProductCategory category = new ProductCategory();
        BeanUtils.copyProperties(categoryParam, category);
        category.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        return this.save(category);
    }
    /**
     * 编辑
     * @param categoryParam
     * @return
     */
    public Boolean edit(CategoryParam categoryParam){
        ProductCategory category = new ProductCategory();
        BeanUtils.copyProperties(categoryParam, category);
        category.setType(null);
        category.setIsSpecial(null);
        return this.updateById(category);
    }

    /**
     * 真删除
     * @param id
     * @return
     */
    public Boolean delById(Integer id){
        // 是否存在子菜单
        if(this.count(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getParentId, id)) > 0){
            throw new BusinessException("当前菜单下存在子权限，请先删除");
        }
        int productCount = productService.count(new LambdaQueryWrapper<Product>().
                eq(Product::getCategoryId, id).eq(Product::getIsDelete, 0));
        if(productCount > 0){
            throw new BusinessException("该分类下存在"+productCount+"个商品，不允许删除");
        }
        return this.removeById(id);
    }

    @Override
    public boolean set(Integer categoryId) {
        ProductCategory bean = this.getById(categoryId);
        if(bean == null){
            return false;
        }
        ProductCategory updateBean = new ProductCategory();
        updateBean.setCategoryId(categoryId);
        updateBean.setStatus(!bean.getStatus());
        return this.updateById(updateBean);
    }

    //商品分类列表
    @Override
    public List<CategoryVo> getIndex(CategoryParam param) {
        List<ProductCategory> list = this.list(new LambdaQueryWrapper<ProductCategory>()
                .eq(ProductCategory::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                //0外卖1店内
                .eq(ProductCategory::getType, param.getType())
                //0普通1特殊
                .eq(ProductCategory::getIsSpecial, param.getIsSpecial())
                .orderByAsc(ProductCategory::getSort)
                .orderByAsc(ProductCategory::getCreateTime));
        return this.transVo(list);
    }

    private List<CategoryVo> transVo(List<ProductCategory> list){
        // 转成vo
        List<CategoryVo> voList = list.stream().map(e -> {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(e, vo);
            vo.setImagePath(uploadFileUtils.getFilePath(vo.getImageId()));
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }
}
