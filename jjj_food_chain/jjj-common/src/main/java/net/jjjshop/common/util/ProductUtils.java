package net.jjjshop.common.util;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.*;
import net.jjjshop.common.param.product.AttrParam;
import net.jjjshop.common.param.product.FeedParam;
import net.jjjshop.common.param.product.ProductParam;
import net.jjjshop.common.service.product.*;
import net.jjjshop.common.vo.product.ProductAttrVo;
import net.jjjshop.common.vo.product.ProductImageVo;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.util.ShopLoginUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductUtils {
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductAttributeService attributeService;
    @Autowired
    private ProductCategoryService categoryService;
    @Autowired
    private ProductFeedService feedService;
    @Autowired
    private ProductUnitService unitService;
    @Autowired
    private SpecService specService;

    /**
     * 商品规格数据
     * @param product
     * @return
     */
    public Map<String, Object> getSpecData(Product product, List<ProductSkuVo> skuList){
        if(product == null || product.getSpecType() != 20){
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        Map<Integer,JSONObject> specAttrData = new HashMap<>();
        List<JSONObject> specListData = new ArrayList<>();
        skuList.forEach(item ->{
            JSONObject sku = new JSONObject();
            sku.put("productSkuId", item.getProductSkuId());
            sku.put("specSkuId", item.getSpecSkuId());
            sku.put("rows", new JSONArray());
            sku.put("productStock",item.getStockNum());
            JSONObject specForm = new JSONObject();
            specForm.put("imageId", item.getImageId());
            specForm.put("imagePath", item.getImagePath());
            specForm.put("productNo", item.getProductNo());
            specForm.put("productPrice", item.getProductPrice());
            specForm.put("productWeight", item.getProductWeight());
            specForm.put("linePrice", item.getLinePrice());
            specForm.put("stockNum", item.getStockNum());
            sku.put("specForm", specForm);
            specListData.add(sku);
        });
        result.put("specAttr", specAttrData.values().stream().collect(Collectors.toList()));
        result.put("specList", specListData);
        return result;
    }

    private String getSpecName(ProductSkuVo sku){
        Map<Integer, String> map = new HashMap<>();
        StringBuilder specName = new StringBuilder();
        List<String> valIds = Arrays.asList(sku.getSpecSkuId().split("_"));
        if(valIds.size() < 2) {
            specName.append(map.get(Integer.valueOf(valIds.get(0))));
        }else {
            for (int i = 0; i < valIds.size(); i++) {
                if(i != valIds.size()-1) {
                    specName.append(map.get(Integer.valueOf(valIds.get(i)))).append(" ");
                }else {
                    specName.append(map.get(Integer.valueOf(valIds.get(i))));
                }
            }
        }
        return specName.toString();
    }

    /**
     * 根据商品id查询sku
     * @param productId
     * @return
     */
    public List<ProductSkuVo> getSkuByProductId(Integer productId){
        List<ProductSku> skuList = productSkuService.list(new LambdaQueryWrapper<ProductSku>()
                .eq(ProductSku::getProductId, productId).orderByAsc(ProductSku::getProductSkuId));
        return skuList.stream().map(e -> {
            ProductSkuVo productSkuVo = new ProductSkuVo();
            BeanUtils.copyProperties(e, productSkuVo);
            productSkuVo.setLinePrice(e.getProductPrice());
            if(e.getImageId() != 0){
                productSkuVo.setImagePath(uploadFileUtils.getFilePath(e.getImageId()));
            }
            return productSkuVo;
        }).collect(Collectors.toList());
    }

    /**
     * 根据id和图片类型获取图片
     * @param productId  商品id
     * @param imageType  图片类型
     * @return
     */
    public List<ProductImageVo> getListByProductId(Integer productId, Integer imageType){
        List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, productId)
                .orderByAsc(ProductImage::getId));
        return imageList.stream().map(e -> {
            ProductImageVo productImageVo = new ProductImageVo();
            BeanUtils.copyProperties(e, productImageVo);
            productImageVo.setFilePath(uploadFileUtils.getFilePath(e.getImageId()));
            return productImageVo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取规格名
     * @return
     */
    public String getProductAttr(Integer productId, String specSkuId, Integer specType){
        String productAttr = "";
        if (specType == 20) {
            String[] attrs = specSkuId.split("_");
        }
        return productAttr;
    }

    public ProductSkuVo getProductSku(Integer productId, Integer specType, Integer productSkuId){
        List<ProductSkuVo> skuList = this.getSkuByProductId(productId);
        // 获取指定的sku
        ProductSkuVo productSku = null;
        for(ProductSkuVo item:skuList){
            if(item.getProductSkuId().equals(productSkuId)){
                productSku = item;
                break;
            }
        }
        if (productSku == null) {
            return null;
        }
        return productSku;
    }

    /**
     * 通过id获取sku
     * @param productSkuId
     * @param specType
     * @return
     */
    public ProductSkuVo getProductSkuById(Integer productSkuId, Integer specType){

        ProductSku productSku = productSkuService.getById(productSkuId);
        // 获取指定的sku
        ProductSkuVo vo = new ProductSkuVo();
        BeanUtils.copyProperties(productSku, vo);
        // 多规格文字内容
        vo.setProductAttr(this.getProductAttr(vo.getProductId(), productSku.getSpecSkuId(), specType));
        return vo;
    }

    //商品属性列表
    public List<ProductAttrVo> getAttribute(Integer shopSupplierId) {
        LambdaQueryWrapper<ProductAttribute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductAttribute::getShopSupplierId, shopSupplierId);
        wrapper.orderByAsc(ProductAttribute::getSort);
        wrapper.orderByDesc(ProductAttribute::getCreateTime);
        List<ProductAttribute> list = attributeService.list(wrapper);
        List<ProductAttrVo> voList = new ArrayList<>();
        for(ProductAttribute bean : list){
            ProductAttrVo vo = new ProductAttrVo();
            BeanUtil.copyProperties(bean, vo);
            if(StringUtils.isNotEmpty(bean.getAttributeValue())){
                //字符串转数组
                vo.setAttributeValueList(Arrays.stream(bean.getAttributeValue().split("\\|")).collect(Collectors.toList()));
            }
            voList.add(vo);
        }
        return voList;
    }

    //商品普通分类列表
    public List<ProductCategory> getCategory(Integer type, Integer shopSupplierId) {
        List<ProductCategory> list = categoryService.list(new LambdaQueryWrapper<ProductCategory>()
                .eq(ProductCategory::getShopSupplierId, shopSupplierId)
                //0外卖1店内
                .eq(ProductCategory::getType, type)
                //0普通1特殊
                .eq(ProductCategory::getIsSpecial, 0)
                .orderByAsc(ProductCategory::getSort)
                .orderByAsc(ProductCategory::getCreateTime));
        return list;
    }

    //商品加料列表
    public List<ProductFeed> getfeed(Integer shopSupplierId) {
        LambdaQueryWrapper<ProductFeed> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFeed::getShopSupplierId,shopSupplierId);
        wrapper.orderByAsc(ProductFeed::getSort);
        wrapper.orderByDesc(ProductFeed::getCreateTime);
        List<ProductFeed> list = feedService.list(wrapper);
        return list;
    }

    //商品规格列表
    public List<Spec> getSpec(Integer shopSupplierId) {
        LambdaQueryWrapper<Spec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spec::getShopSupplierId, shopSupplierId);
        wrapper.orderByAsc(Spec::getSort);
        wrapper.orderByDesc(Spec::getCreateTime);
        List<Spec> list = specService.list(wrapper);
        return list;
    }

    //商品特殊分类列表
    public List<ProductCategory> getspecial(Integer type, Integer shopSupplierId) {
        List<ProductCategory> list = categoryService.list(new LambdaQueryWrapper<ProductCategory>()
                .eq(ProductCategory::getShopSupplierId, shopSupplierId)
                //0外卖1店内
                .eq(ProductCategory::getType, type)
                //0普通1特殊
                .eq(ProductCategory::getIsSpecial, 1)
                .orderByAsc(ProductCategory::getSort)
                .orderByAsc(ProductCategory::getCreateTime));
        return list;
    }

    //商品单位列表
    public List<ProductUnit> getunit(Integer shopSupplierId) {
        LambdaQueryWrapper<ProductUnit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductUnit::getShopSupplierId,shopSupplierId);
        wrapper.orderByAsc(ProductUnit::getSort);
        wrapper.orderByDesc(ProductUnit::getCreateTime);
        List<ProductUnit> list = unitService.list(wrapper);
        return list;
    }

    //更新属性
    public void updateAttr(List<AttrParam> productAttr, Integer shopSupplierId) {
        List<ProductAttribute> list = new ArrayList<>();
        for(AttrParam param : productAttr){
            int count = attributeService.count(new LambdaQueryWrapper<ProductAttribute>()
                    .eq(ProductAttribute::getAttributeName,param.getAttributeName())
                    .eq(ProductAttribute::getShopSupplierId,shopSupplierId));
            if(count == 0){
                if(CollectionUtils.isEmpty(param.getAttributeValue())){
                    throw new BusinessException("属性值不能为空");
                }

                ProductAttribute bean = new ProductAttribute();
                BeanUtils.copyProperties(param,bean);
                //数组转字符串
                bean.setAttributeValue(String.join("|", param.getAttributeValue()));
                bean.setShopSupplierId(shopSupplierId);
                list.add(bean);
            }
        }
        if(CollectionUtils.isNotEmpty(list)){
            attributeService.saveBatch(list);
        }
    }

    //更新商品规格
    public void updatespec(List<ProductParam.SkuParam> specList, Integer shopSupplierId) {
        List<Spec> list = new ArrayList<>();
        for(ProductParam.SkuParam param : specList){
            if(StringUtils.isEmpty(param.getSpecName())){
                continue;
            }
            int count = specService.count(new LambdaQueryWrapper<Spec>()
                    .eq(Spec::getSpecName,param.getSpecName())
                    .eq(Spec::getShopSupplierId,shopSupplierId));
            if(count == 0){
                Spec bean = new Spec();
                BeanUtils.copyProperties(param,bean);
                bean.setShopSupplierId(shopSupplierId);
                list.add(bean);
            }
        }
        if(CollectionUtils.isNotEmpty(list)){
            specService.saveBatch(list);
        }
    }

    //更新加料
    public void updateFeed(List<FeedParam> productFeed, Integer shopSupplierId) {
        List<ProductFeed> list = new ArrayList<>();
        for(FeedParam param : productFeed){
            int count = feedService.count(new LambdaQueryWrapper<ProductFeed>()
                    .eq(ProductFeed::getFeedName,param.getFeedName())
                    .eq(ProductFeed::getShopSupplierId,shopSupplierId));
            if(count == 0){
                ProductFeed bean = new ProductFeed();
                BeanUtils.copyProperties(param,bean);
                bean.setShopSupplierId(shopSupplierId);
                list.add(bean);
            }
        }
        if(CollectionUtils.isNotEmpty(list)){
            feedService.saveBatch(list);
        }
    }

    //更新单位
    public void updateUnit(String productUnit, Integer shopSupplierId) {
        int count = unitService.count(new LambdaQueryWrapper<ProductUnit>()
                .eq(ProductUnit::getUnitName,productUnit)
                .eq(ProductUnit::getShopSupplierId,shopSupplierId));
        if(count == 0){
            ProductUnit bean = new ProductUnit();
            bean.setUnitName(productUnit);
            bean.setShopSupplierId(shopSupplierId);
            unitService.save(bean);
        }
    }
}
