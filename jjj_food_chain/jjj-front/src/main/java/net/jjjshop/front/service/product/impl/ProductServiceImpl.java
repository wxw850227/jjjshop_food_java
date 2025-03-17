package net.jjjshop.front.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.mapper.product.ProductMapper;
import net.jjjshop.common.service.product.ProductCategoryService;
import net.jjjshop.common.service.product.ProductSkuService;
import net.jjjshop.common.settings.vo.PointsVo;
import net.jjjshop.common.settings.vo.RecommendVo;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.LoginUtil;
import net.jjjshop.front.mapper.user.UserCartMapper;
import net.jjjshop.front.param.order.CategoryListParam;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.user.UserCartService;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.common.vo.product.ProductListVo;
import net.jjjshop.front.vo.product.ProductUserCartVo;
import net.jjjshop.front.vo.product.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-01
 */
@Slf4j
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private UserCartService userCartService;
    @Autowired
    private ProductCategoryCache productCategoryCache;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private UserCartMapper userCartMapper;

    /**
     * 商品详情
     * @param product
     * @param user
     * @return
     */
    public ProductVo getDetail(Product product, User user) {
        // 商品详情
        if (product == null || product.getIsDelete() == 1 || product.getProductStatus() != 10) {
            throw new BusinessException("很抱歉，商品信息不存在或已下架");
        }
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);
        // skuList
        vo.setSkuList(productUtils.getSkuByProductId(vo.getProductId()));
        // sku
        vo.setProductSku(this.getShowSku(vo));
        // image
        vo.setImage(productUtils.getListByProductId(vo.getProductId(), 0));
        // 实际销量
        vo.setProductSales(vo.getSalesActual() + vo.getSalesInitial());
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
        // 访问量
        this.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, product.getProductId())
                .setSql("`view_times` = `view_times` + 1 "));
        return vo;
    }

    /**
     * 商品转成商品列表显示vo
     * @param product
     * @return
     */
    private ProductListVo transProductListData(Product product) {
        ProductListVo vo = new ProductListVo();
        BeanUtil.copyProperties(product, vo);
        vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
        // 实际销量
        vo.setProductSales(product.getSalesActual() + product.getSalesInitial());

        // skuList
        vo.setSkuList(productUtils.getSkuByProductId(vo.getProductId()));
        // sku
        vo.setProductSku(this.getShowSkuOne(vo));
        // image
        vo.setImage(productUtils.getListByProductId(vo.getProductId(), 0));


        return vo;
    }

    /**
     * 设置商品的会员价
     * @param vo
     * @param user
     * @return
     */
    public void setProductGradeMoney(ProductVo vo, User user) {
        vo.setIsUserGrade(false);
        if (user == null || user.getGradeId() == 0) {
            return;
        }
        // 判断商品是否参与会员折扣
        if (vo.getIsEnableGrade() == 0) {
            return;
        }
        BigDecimal discountRatio = null;
        int aloneGradeType = 10;
        // 商品单独设置了会员折扣
        JSONObject aloneGradeEquity = JSON.parseObject(vo.getAloneGradeEquity());
        if (vo.getIsAloneGrade() == 1 &&
                StringUtils.isNotEmpty(aloneGradeEquity.getString("" + user.getGradeId()))) {
            if (vo.getAloneGradeType() == 10) {
                // 折扣比例
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(new BigDecimal(100));
            } else {
                aloneGradeType = 20;
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(vo.getProductPrice(), BigDecimal.ROUND_CEILING);
            }
        } else {
            // 折扣比例
        }
        if (discountRatio.compareTo(BigDecimal.ONE) < 0) {
            // 标记参与会员折扣
            vo.setIsUserGrade(true);
            // 会员折扣后的商品总金额
//            if (aloneGradeType == 20) {
//                vo.setProductPrice(aloneGradeEquity.getBigDecimal("" + user.getGradeId()));
//                vo.setHighPrice(aloneGradeEquity.getBigDecimal("" + user.getGradeId()));
//            } else {
//                vo.setProductPrice(vo.getProductPrice().multiply(discountRatio));
//                vo.setHighPrice(vo.getHighPrice().multiply(discountRatio));
//            }

            // 会员折扣价
            for (ProductSkuVo skuVo : vo.getSkuList()) {
                if (aloneGradeType == 20) {
                    skuVo.setProductPrice(BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())));
                } else {
                    skuVo.setProductPrice(skuVo.getProductPrice().multiply(discountRatio));
                }
            }
        } else {
            vo.setIsUserGrade(false);
        }
    }

    /**
     * 获取显示sku
     * @param vo
     * @return
     */
    private ProductSkuVo getShowSku(ProductVo vo) {
        if (vo.getSpecType() == 10) {
            return vo.getSkuList().get(0);
        } else {
            //多规格返回最低价
            for (ProductSkuVo sku : vo.getSkuList()) {
                if (vo.getProductPrice().compareTo(sku.getProductPrice()) == 0) {
                    return sku;
                }
            }
        }
        // 如果找不到返回第一个
        return vo.getSkuList().get(0);
    }

    private ProductSkuVo getShowSkuOne(ProductListVo vo) {
        if (vo.getSpecType() == 10) {
            return vo.getSkuList().get(0);
        } else {
            //多规格返回最低价
            for (ProductSkuVo sku : vo.getSkuList()) {
                if (vo.getProductPrice().compareTo(sku.getProductPrice()) == 0) {
                    return sku;
                }
            }
        }
        // 如果找不到返回第一个
        return vo.getSkuList().get(0);
    }

    /**
     * 购买商品信息
     * @param masterOrderBuyParam
     * @return
     */
    public List<ProductBuyVo> getOrderProductListByNow(OrderBuyParam masterOrderBuyParam) {
        Product product = this.getById(masterOrderBuyParam.getProductId());
        ProductBuyVo vo = new ProductBuyVo();
        BeanUtils.copyProperties(product, vo);
        vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
        // 转list兼容购物车
        List<ProductBuyVo> list = new ArrayList<>();
        list.add(vo);
        return list;
    }

    /**
     * 商品查询
     * @param productParam
     * @param user
     * @return
     */
    public Paging<ProductListVo> getList(ProductParam productParam, User user) {
        QueryWrapper<Product> wrapper = new QueryWrapper<Product>();
        wrapper.lambda().eq(Product::getIsDelete, 0).eq(Product::getProductStatus, 10);
        // 名称搜索
        if (StringUtils.isNotEmpty(productParam.getSearch())) {
            wrapper.lambda().like(Product::getProductName, productParam.getSearch());
        }
        // 分类搜索
        if (productParam.getCategoryId() != null && productParam.getCategoryId() > 0) {
            List<Integer> subCategoryIds = productCategoryCache.getSubCategoryId(productParam.getCategoryId());
            wrapper.lambda().in(Product::getCategoryId, subCategoryIds);
        }
        // 排序
        if ("sales".equals(productParam.getSortType())) {
            wrapper.orderByDesc("sales_initial + sales_actual");
        } else if ("price".equals(productParam.getSortType())) {
            if (productParam.getSortPrice() == 0) {
                wrapper.lambda().orderByDesc(Product::getProductPrice);
            } else {
                wrapper.lambda().orderByAsc(Product::getProductPrice);
            }
        } else {
            wrapper.lambda().orderByAsc(Product::getProductSort).orderByDesc(Product::getCreateTime);
        }
        Page<Product> page = new PageInfo<>(productParam);
        IPage<Product> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductListVo> resultPage = iPage.convert(result -> {
            return this.transProductListData(result);
        });
        return new Paging(resultPage);
    }

    /**
     * 商品推荐
     * @param location
     * @param user
     * @return
     */
    public Map<String, Object> getRecommend(Integer location, User user) {
        Map<String, Object> result = new HashMap<>();
        // 获取配置
        JSONObject vo = settingUtils.getSetting(SettingEnum.RECOMMEDND.getKey(), null);
        RecommendVo recommendVo = JSONObject.toJavaObject(vo, RecommendVo.class);
        Boolean isRecommend = this.checkRecommend(recommendVo, location);
        if (isRecommend) {
            // 推荐名称
            result.put("recommendName", recommendVo.getName());
            // 推荐商品
            result.put("list", this.getRecommendProduct(recommendVo));
        }
        result.put("isRecommend", isRecommend);
        return result;
    }

    /**
     * 是否展示商品推荐
     * @param recommendVo
     * @param location
     * @return
     */
    private Boolean checkRecommend(RecommendVo recommendVo, Integer location) {
        Boolean isRecommend = true;
        if (recommendVo.getIsRecommend().intValue() > 0) {
            if(recommendVo.getLocation() == null){
                return false;
            }
            if (!Arrays.asList(recommendVo.getLocation()).contains(location)) {
                isRecommend = false;
            }
        } else {
            isRecommend = false;
        }
        return isRecommend;
    }

    private List<ProductListVo> getRecommendProduct(RecommendVo recommendVo) {
        List<Product> productList = null;
        // 手动选择
        if (recommendVo.getChoice().intValue() == 1) {
            List<Integer> productIds = new ArrayList<>();
            for (int i = 0; i < recommendVo.getProduct().length; i++) {
                productIds.add(recommendVo.getProduct()[i].getProductId());
            }
            productList = this.list(new LambdaQueryWrapper<Product>().in(Product::getProductId, productIds)
                    .eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0));
        } else {
            LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0).last("LIMIT 0," + recommendVo.getNum());
            // 排序
            if (recommendVo.getType().intValue() == 10) {
                // 销量排序
                wrapper.orderByDesc(Product::getSalesActual);
            } else if (recommendVo.getType().intValue() == 20) {
                // 时间排序
                wrapper.orderByDesc(Product::getCreateTime);
            } else if (recommendVo.getType().intValue() == 30) {
                // 查看次数排序
                wrapper.orderByDesc(Product::getViewTimes);
            }
            productList = this.list(wrapper);
        }
        return productList.stream().map(e -> {
            return this.transProductListData(e);
        }).collect(Collectors.toList());
    }

    /**
     * 通过商品id集合获取商品列表
     * @param productIds
     * @param user
     * @return
     */
    public List<ProductListVo> getListByProductIds(List<Integer> productIds, User user) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (productIds != null && productIds.size() > 0) {
            wrapper.in(Product::getProductId, productIds);
        }
        wrapper.eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0);
        List<Product> productList = this.list(wrapper);
        return productList.stream().map(e -> {
            return this.transProductListData(e);
        }).collect(Collectors.toList());
    }

    /**
     * 通过商品分类id集合获取商品列表
     * @param categoryId
     * @return
     */
    public List<ProductListVo> getListByCategoryIds(Integer categoryId, CategoryListParam param) {
        ProductCategory category = productCategoryService.getById(categoryId);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        //0普通1特殊
        if(category.getIsSpecial()){
            //特殊分类id
            wrapper.eq(Product::getSpecialId, categoryId);
        }else {
            //产品分类id
            wrapper.eq(Product::getCategoryId, categoryId);
        }

        wrapper.eq(Product::getProductStatus, 10).eq(Product::getIsDelete, 0);

        List<Product> productList = this.list(wrapper);

        return productList.stream().map(e -> {
            return this.transCategoryProductList(e,param);
        }).collect(Collectors.toList());
    }

    private ProductListVo transCategoryProductList(Product product,CategoryListParam productParam) {
        ProductListVo vo = new ProductListVo();
        BeanUtil.copyProperties(product, vo);
        vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
        // 实际销量
        vo.setProductSales(product.getSalesActual() + product.getSalesInitial());

        // skuList
        vo.setSkuList(productUtils.getSkuByProductId(vo.getProductId()));
        // sku
        vo.setProductSku(this.getShowSkuOne(vo));
        // image
        vo.setImage(productUtils.getListByProductId(vo.getProductId(), 0));
        Integer userId = LoginUtil.getUserId();
        //购物车数量
        if(userId != null && userId != 0){
            productParam.setUserId(userId);
            productParam.setProductId(product.getProductId());
            vo.setCartNum(userCartMapper.getCartNum(productParam));
        }
        //是否多属性
        if(vo.getSpecType() == 20 || StringUtils.isNotEmpty(vo.getProductAttr())
                || StringUtils.isNotEmpty(vo.getProductFeed())){
            //产品属性(10单属性 20多属性)
            vo.setSpecTypes(20);
        }
        return vo;
    }


    /**
     * 购物车商品
     * @param user
     * @param orderBuyParam
     * @return
     */
    public List<ProductBuyVo> getOrderProductListByCart(User user, OrderBuyParam orderBuyParam) {
        orderBuyParam.setUserId(user.getUserId());
        orderBuyParam.setCartIdList(Arrays.stream(orderBuyParam.getCartIds().split(",")).map(Integer::valueOf).collect(Collectors.toList()));
        List<ProductBuyVo> productList = userCartService.getCartList(orderBuyParam);
        for(ProductBuyVo vo : productList){
            vo.setSku(productSkuService.getById(vo.getProductSkuId()));
        }
        return productList;
    }

    // 优惠信息
    @Override
    public Map<String, Object> getDiscount(Integer productId) {
        Map<String, Object> discount = new HashMap<>();

        boolean showDiscount = false;

        discount.put("showDiscount",showDiscount);
        return discount;
    }

    //商品赠送积分
    private Integer getGivePoints(Integer productId) {
        // 积分设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.POINTS.getKey(), null);
        PointsVo pointsVo = JSONObject.parseObject(vo.toJSONString(), PointsVo.class);
        // 条件：后台是否开启开启购物送积分
        if (!pointsVo.getIsShoppingDiscount()) {
            return 0;
        }
        Product product = this.getById(productId);
        Integer totalPointsBonus = product.getProductPrice().multiply(new BigDecimal(pointsVo.getGiftRatio())).divide(new BigDecimal(100),1, RoundingMode.DOWN).intValue();
        return totalPointsBonus;
    }
}
