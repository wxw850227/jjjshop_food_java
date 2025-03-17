package net.jjjshop.shop.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.mapper.product.ProductMapper;
import net.jjjshop.common.param.product.AttrParam;
import net.jjjshop.common.param.product.FeedParam;
import net.jjjshop.common.param.product.ProductParam;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.service.user.UserCartService;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.product.*;
import net.jjjshop.shop.service.product.*;
import net.jjjshop.shop.vo.product.ProductVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UserCartService userCartService;

    /**
     * 商品列表
     * @param productPageParam
     * @return
     */
    public Map<String,Object> getList(ProductPageParam productPageParam){
        Map<String,Object> result = new HashMap<>();
        // 商品列表
        Page<Product> page = new PageInfo<>(productPageParam);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getIsDelete, 0);
        wrapper.eq(Product::getShopSupplierId, ShopLoginUtil.getShopSupplierId());
        //名称搜索
        if(StringUtils.isNotEmpty(productPageParam.getProductName())){
            wrapper.like(Product::getProductName, productPageParam.getProductName());
        }
        //分类搜索
        if(productPageParam.getCategoryId() != null && productPageParam.getCategoryId() > 0){
            wrapper.eq(Product::getCategoryId,productPageParam.getCategoryId());
        }
        //0外卖1店内
        if(productPageParam.getProductType() != null){
            wrapper.eq(Product::getProductType,productPageParam.getProductType());
        }
        // 类型
        if(StringUtils.isNotEmpty(productPageParam.getType())){
            this.setWrapperByType(wrapper, productPageParam.getType());
        }
        wrapper.orderByAsc(Product::getProductSort);
        wrapper.orderByDesc(Product::getCreateTime);
        IPage<Product> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductVo> resultPage = iPage.convert(item -> {
            ProductVo vo = new ProductVo();
            BeanUtil.copyProperties(item, vo);
            vo.setImagePath(this.getImagePath(vo.getProductId()));
            vo.setCategoryName(productCategoryService.getById(vo.getCategoryId()) == null ? "" :
                    productCategoryService.getById(vo.getCategoryId()).getName());
            return vo;
        });
        result.put("list", new Paging(resultPage));
        // 商品统计数量
        JSONObject productCount = new JSONObject();
        //出售中
        productCount.put("sell", this.getCount("sell",productPageParam.getProductType()));
        //仓库中
        productCount.put("lower", this.getCount("lower",productPageParam.getProductType()));
        result.put("productCount", productCount);
        //分类列表
        CategoryParam param = new CategoryParam();
        //0外卖1店内
        param.setType(productPageParam.getProductType());
        //0普通1特殊
        param.setIsSpecial(0);
        result.put("category", productCategoryService.getIndex(param));
        return result;
    }


    /**
     * 获取数量
     * @param type
     * @return
     */
    private Integer getCount(String type,Integer ProductType){
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        this.setWrapperByType(wrapper, type);
        wrapper.eq(Product::getIsDelete, 0);
        wrapper.eq(Product::getProductType, ProductType);
        return this.count(wrapper);
    }

    /**
     * 根据类型设置查询条件
     * @param wrapper
     * @param type
     * @return
     */
    private void setWrapperByType(LambdaQueryWrapper<Product> wrapper, String type){
        // 销售中
        if (type.equalsIgnoreCase("sell")) {
            wrapper.eq(Product::getProductStatus, 10);
        }
        //仓库中
        if (type.equalsIgnoreCase("lower")) {
            wrapper.eq(Product::getProductStatus, 20);
        }
    }

    /**
     * 通过商品id查询主图
     * @param productId
     * @return
     */
    public String getImagePath(Integer productId){
        List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, productId).last("limit 1").orderByAsc(ProductImage::getId));
        if(imageList.size() == 0){
            return "";
        }else{
            return uploadFileUtils.getFilePath(imageList.get(0).getImageId());
        }
    }

    /**
     * 新增商品
     * @param productParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ProductParam productParam){
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        if(StringUtils.isNotEmpty(product.getAloneGradeEquity())){
            product.setAloneGradeEquity(StringEscapeUtils.unescapeHtml4(product.getAloneGradeEquity()));
        }
        product.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        this.setProductParams(productParam, product);
        // 保存商品
        this.save(product);
        // 保存商品规格
        productParam.setProductId(product.getProductId());
        this.addProductSpec(productParam, false, null);
        // 保存商品图片
        this.addProductImages(productParam, product, 0);
        return true;
    }

    /**
     * 修改商品
     * @param productParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(ProductParam productParam){
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        if(StringUtils.isNotEmpty(product.getAloneGradeEquity())){
            product.setAloneGradeEquity(StringEscapeUtils.unescapeHtml4(product.getAloneGradeEquity()));
        }
        product.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        this.setProductParams(productParam, product);
        // 保存商品
        this.updateById(product);
        // 原sku
        List<ProductSku> skuList = productSkuService.list(new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getProductId, productParam.getProductId()));
        // 保存商品规格
        this.addProductSpec(productParam, true, skuList);
        // 保存商品图片
        this.addProductImages(productParam, product, 0);
        return true;
    }

    /**
     * 添加商品规格
     * @param productParam
     * @param isUpdate
     * @param skuList
     * @return
     */
    private void addProductSpec(ProductParam productParam, boolean isUpdate, List<ProductSku> skuList)
    {
        // 添加规格数据
        if (productParam.getSpecType() == 10) {
            // 单规格
            if(isUpdate){
                //编辑
                ProductParam.SkuParam item = productParam.getSku().get(0);
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(item,sku);
                productSkuService.updateById(sku);
                //更新购物车商品价格
                userCartService.updaterCart(sku);
            }else {
                //新增
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(productParam.getSku().get(0), sku);
                sku.setProductId(productParam.getProductId());
                sku.setLinePrice(sku.getProductPrice());
                productSkuService.save(sku);
            }
        }else if (productParam.getSpecType() == 20) {
            // 添加商品sku
            this.addSkuList(productParam, skuList);
        }
    }

    /**
     * 新增sku
     * @param productParam
     * @param skuList
     * @return
     */
    private void addSkuList(ProductParam productParam, List<ProductSku> skuList){
        List<ProductParam.SkuParam> specList = productParam.getSku();
        List<ProductSku> updateData = new ArrayList<>();
        List<ProductSku> saveData = new ArrayList<>();
        for(int i=0;i<specList.size();i++){
            ProductParam.SkuParam item = specList.get(i);
            ProductSku sku = new ProductSku();
            BeanUtils.copyProperties(item,sku);
            sku.setProductId(productParam.getProductId());
            if(specList.get(i).getProductSkuId() != null && specList.get(i).getProductSkuId() > 0 && !("copy".equals(productParam.getScene()))){
                for(int j=0;j<skuList.size();j++){
                    if(skuList.get(j).getProductSkuId().intValue() == specList.get(i).getProductSkuId().intValue()){
                        skuList.remove(j);
                        break;
                    }
                }
                //保留的规格
                updateData.add(sku);
                //更新购物车商品价格
                userCartService.updaterCart(sku);
            } else{
                //新增的规格
                saveData.add(sku);
            }
        }
        if(updateData.size() > 0){
            productSkuService.updateBatchById(updateData);
        }
        if(saveData.size() > 0){
            productSkuService.saveBatch(saveData);
        }
        // 未保留的删除
        if(CollectionUtils.isNotEmpty(skuList)){
            List<Integer> idList = new ArrayList<>();
            skuList.forEach(item->{
                idList.add(item.getProductSkuId());
            });
            productSkuService.remove(new LambdaQueryWrapper<ProductSku>().in(ProductSku::getProductSkuId, idList));
        }
    }

    private void setProductParams(ProductParam productParam, Product product){
        //规格
        List<ProductParam.SkuParam> specList = productParam.getSku();
        if(CollectionUtils.isEmpty(specList)){
            throw new BusinessException("请填写规格");
        }
        if (productParam.getSpecType() == 10) {
            // 单规格
            //产品总库存
            product.setProductStock(specList.get(0).getStockNum());
            //产品一口价
            product.setProductPrice(specList.get(0).getProductPrice());
            //划线价
            product.setLinePrice(specList.get(0).getProductPrice());
            //成本价
            product.setCostPrice(specList.get(0).getCostPrice());
            //包装费
            product.setBagPrice(specList.get(0).getBagPrice());
        }else if (productParam.getSpecType() == 20){
            // 多规格
            ProductParam.SkuParam specForm = specList.get(0);
            BigDecimal productPrice = specForm.getProductPrice();
            BigDecimal costPrice = specForm.getCostPrice();
            BigDecimal bagPrice = specForm.getBagPrice();
            Integer stock = 0;
            // 取最低价
            for(int i=0;i<specList.size();i++){
                ProductParam.SkuParam item = specList.get(i);
                stock += item.getStockNum();
                if(item.getProductPrice().compareTo(productPrice) < 0){
                    productPrice = item.getProductPrice();
                }
                if(item.getCostPrice().compareTo(costPrice) < 0){
                    costPrice = item.getCostPrice();
                }
                if(item.getBagPrice().compareTo(bagPrice) < 0){
                    bagPrice = item.getBagPrice();
                }
            }
            //产品总库存
            product.setProductStock(stock);
            //产品一口价
            product.setProductPrice(productPrice);
            //划线价
            product.setLinePrice(productPrice);
            //成本价
            product.setCostPrice(costPrice);
            //包装费
            product.setBagPrice(bagPrice);
        }
        //更新属性
        List<AttrParam> productAttr = productParam.getProductAttrList();
        if(CollectionUtils.isNotEmpty(productAttr)){
            product.setProductAttr(JSON.toJSONString(productAttr));
            productUtils.updateAttr(productAttr,product.getShopSupplierId());
        }else {
            product.setProductAttr("");
        }
        //更新商品规格
        productUtils.updatespec(specList,product.getShopSupplierId());
        //更新加料
        List<FeedParam> productFeed = productParam.getProductFeedList();
        if(CollectionUtils.isNotEmpty(productFeed)){
            product.setProductFeed(JSON.toJSONString(productFeed));
            productUtils.updateFeed(productFeed,product.getShopSupplierId());
        }else {
            product.setProductFeed("");
        }
        //更新单位
        productUtils.updateUnit(product.getProductUnit(),product.getShopSupplierId());
    }

    /**
     * 添加商品图片
     * imageType 0商品主图，1详情图
     * @param productParam
     * @param product
     * @param imageType
     * @return
     */
    private void addProductImages(ProductParam productParam, Product product, Integer imageType)
    {
        // 先删除图片
        productImageService.remove(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, product.getProductId()));
        List<ProductImage> list = new ArrayList<>();
        List<ProductParam.ImageParam> imageList = null;
        if(imageType == 0){
            imageList = productParam.getImage();
        }
        imageList.forEach(item -> {
            ProductImage image = new ProductImage();
            if(item.getImageId() != null && item.getImageId() > 0){
                image.setImageId(item.getImageId());
            }else {
                image.setImageId(item.getFileId());
            }
            image.setProductId(product.getProductId());
            list.add(image);
        });
        productImageService.saveBatch(list);
    }

    /**
     * 获取新增或修改数据
     * @param productId
     * @param scene
     * @return
     */
    public Map<String, Object> getBaseData(Integer productId, String scene,Integer type){
        Map<String, Object> result = new HashMap<>();
        Product product = null;
        List<ProductSkuVo> skuVoList = null;
        if(productId > 0){
            product = this.getById(productId);
            result.put("model", transVo(product));
        }
        Integer shopSupplierId = ShopLoginUtil.getShopSupplierId();
        //商品属性列表
        result.put("attribute", productUtils.getAttribute(shopSupplierId));
        //商品普通分类列表
        result.put("category", productUtils.getCategory(type,shopSupplierId));
        //商品加料列表
        result.put("feed", productUtils.getfeed(shopSupplierId));
        //商品规格列表
        result.put("spec", productUtils.getSpec(shopSupplierId));
        //商品特殊分类列表
        result.put("special", productUtils.getspecial(type,shopSupplierId));
        //商品单位列表
        result.put("unit", productUtils.getunit(shopSupplierId));

        return result;
    }

    public ProductVo transVo(Product product){
        // 商品信息
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);
        if(StringUtils.isNotEmpty(vo.getAloneGradeEquity())){
            vo.setAloneGradeEquityJson(JSON.parseObject(vo.getAloneGradeEquity()));
        }
        //分类
        vo.setCategory(productCategoryService.getById(product.getCategoryId()));
        //商品属性列表
        vo.setProductAttrList(JSON.parseArray(product.getProductAttr()));
        //商品加料列表
        vo.setProductFeedList(JSON.parseArray(product.getProductFeed()));
        if(vo.getProductAttrList() == null){
            vo.setProductAttrList(new JSONArray());
        }
        if(vo.getProductFeedList() == null){
            vo.setProductFeedList(new JSONArray());
        }
        //店铺
        vo.setSupplier(supplierService.getById(product.getShopSupplierId()));
        // sku规格
        vo.setSku(productUtils.getSkuByProductId(product.getProductId()));
        // image
        vo.setImage(productUtils.getListByProductId(product.getProductId(), 0));
        return vo;
    }

    /**
     * 修改商品状态
     * @param productId
     * @param state
     * @return
     */
    public boolean setState(Integer productId, Integer state){
        return this.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, productId)
                .set(Product::getProductStatus, state));
    }

    /**
     * 商品删除
     * @param productId
     * @return
     */
    public boolean setDelete(Integer productId){
        return this.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, productId)
                .set(Product::getIsDelete, 1));
    }

    /**
     * 商品列表
     * @param productIds
     * @return
     */
    public List<ProductVo> getListByIds(List<Integer> productIds){
        List<Product> productList = this.list(new LambdaQueryWrapper<Product>()
                .in(Product::getProductId, productIds).eq(Product::getIsDelete, 0)
                .eq(Product::getProductStatus, 10));
        return productList.stream().map(e -> {
            ProductVo vo = new ProductVo();
            BeanUtil.copyProperties(e, vo);
            vo.setImagePath(this.getImagePath(vo.getProductId()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 所有商品列表
     * @param
     * @return
     */
    public List<ProductVo> getAll() {
        List<Product> productList = this.list(new LambdaQueryWrapper<Product>()
                .eq(Product::getProductStatus, 10)
                .orderByAsc(Product::getProductSort)
                .orderByDesc(Product::getCreateTime));
        return productList.stream().map(e -> {
            ProductVo vo = new ProductVo();
            BeanUtil.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
