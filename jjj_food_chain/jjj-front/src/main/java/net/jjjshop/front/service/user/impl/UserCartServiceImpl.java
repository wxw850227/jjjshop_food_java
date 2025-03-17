package net.jjjshop.front.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.util.DateUtil;
import net.jjjshop.framework.util.LoginUtil;
import net.jjjshop.front.mapper.user.UserCartMapper;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.product.CartParam;
import net.jjjshop.front.param.product.UserCartParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserCartService;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.front.vo.product.ProductUserCartVo;
import net.jjjshop.front.vo.product.UserCartVo;
import net.jjjshop.front.vo.supplier.SupplierVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户购物车 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */
@Slf4j
@Service
public class UserCartServiceImpl extends BaseServiceImpl<UserCartMapper, UserCart> implements UserCartService {
    @Autowired
    private UserCartMapper userCartMapper;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierService supplierService;

    /**
     * 获取购物车所有商品
     * @param user
     * @return
     */
    @Override
    public List<ProductUserCartVo> getAll(User user, UserCartParam param) {
        if(user == null){
            return null;
        }
        param.setUserId(user.getUserId());
        List<ProductUserCartVo> voList = userCartMapper.getList(param);
        for(int i=voList.size()-1;i>=0;i--){
            ProductUserCartVo item = voList.get(i);
            item.setProductImage(uploadFileUtils.getImagePathByProductId(item.getProductId()));
            Product product = productService.getById(item.getProductId());
            item.setProduct(product);
            if(product != null){
                item.setProductName(product.getProductName());
            }
        }
        return voList;
    }

    @Override
    public List<ProductBuyVo> getCartList(OrderBuyParam orderBuyParam) {
        if(orderBuyParam.getUserId() == null){
            return null;
        }
        List<ProductBuyVo> voList = userCartMapper.getCartList(orderBuyParam);
        for(int i=voList.size()-1;i>=0;i--){
            ProductBuyVo item = voList.get(i);
            item.setProductImage(uploadFileUtils.getImagePathByProductId(item.getProductId()));
            item.setProduct(productService.getById(item.getProduct()));
        }
        return voList;
    }

    /**
     * 编辑分类列表商品数量
     * @return
     */
    @Override
    public boolean productSub(UserCartParam param) {
        SupplierVo supplier = supplierService.getDetailById(param.getShopSupplierId());
        //店铺状态0营业中1停止营业
        if(supplier.getIsDelete() == 1  || supplier.getStatus() == 1){
            throw new BusinessException("店铺休息中");
        }
        //外卖营业时间
        List<String> deliveryTimeList = supplier.getDeliveryTimeList();
        //自取营业时间
        List<String> pickTimeList = supplier.getPickTimeList();
        //店内营业时间
        List<String> storeTimeList = supplier.getStoreTimeList();
        String now = DateUtil.getTimeString(new Date());
        //配送方式(10外卖配送 20上门取 30打包带走 40店内就餐
        switch (param.getDelivery()) {
            //10外卖配送
            case 10:
                if(deliveryTimeList.size() == 2 && !( now.compareTo(deliveryTimeList.get(0)) >= 0 && now.compareTo(deliveryTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在外卖营业时间");
                }
                break;
            //20上门取
            case 20:
                if(pickTimeList.size() == 2 && !( now.compareTo(pickTimeList.get(0)) >= 0 && now.compareTo(pickTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在自提营业时间");
                }
                break;
            //30打包带走
            case 30:
                if(storeTimeList.size() == 2 && !( now.compareTo(storeTimeList.get(0)) >= 0 && now.compareTo(storeTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在店内营业时间");
                }
                break;
            //40店内就餐
            case 40:
                if(storeTimeList.size() == 2 && !( now.compareTo(storeTimeList.get(0)) >= 0 && now.compareTo(storeTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在店内营业时间");
                }
                break;
        }
        Integer userId = LoginUtil.getUserId();
        param.setUserId(userId);
        UserCart cart = this.getUserCart(userId, param.getProductId());
        //添加:up, 减少:down
        if("down".equals(param.getType())){
            if (cart.getProductNum().intValue() > 1) {
                // 更新
                UserCart updateBean = new UserCart();
                updateBean.setCartId(cart.getCartId());
                updateBean.setProductNum(cart.getProductNum() - 1);
                return this.updateById(updateBean);
            } else {
                // 删除
                return this.removeById(cart.getCartId());
            }
        }else {
            UserCart updateBean = new UserCart();
            updateBean.setCartId(cart.getCartId());
            updateBean.setProductNum(cart.getProductNum() + 1);
            return this.updateById(updateBean);
        }
    }

    public UserCart getUserCart(Integer userId, Integer productId) {
        return this.list(new LambdaQueryWrapper<UserCart>()
                .eq(UserCart::getUserId, userId)
                .eq(UserCart::getProductId, productId)).get(0);
    }

    /**
     * 添加商品
     * @param user
     * @param param
     * @return
     */
    @Override
    public Boolean add(User user, UserCartParam param) {
        SupplierVo supplier = supplierService.getDetailById(param.getShopSupplierId());
        //店铺状态0营业中1停止营业
        if(supplier.getIsDelete() == 1  || supplier.getStatus() == 1){
            throw new BusinessException("店铺休息中");
        }
        //外卖营业时间
        List<String> deliveryTimeList = supplier.getDeliveryTimeList();
        //自取营业时间
        List<String> pickTimeList = supplier.getPickTimeList();
        //店内营业时间
        List<String> storeTimeList = supplier.getStoreTimeList();
        String now = DateUtil.getTimeString(new Date());
        //配送方式(10外卖配送 20上门取 30打包带走 40店内就餐
        switch (param.getDelivery()) {
            //10外卖配送
            case 10:
                if(deliveryTimeList.size() == 2 && !( now.compareTo(deliveryTimeList.get(0)) >= 0 && now.compareTo(deliveryTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在外卖营业时间");
                }
                break;
            //20上门取
            case 20:
                if(pickTimeList.size() == 2 && !( now.compareTo(pickTimeList.get(0)) >= 0 && now.compareTo(pickTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在自提营业时间");
                }
                break;
            //30打包带走
            case 30:
                if(storeTimeList.size() == 2 && !( now.compareTo(storeTimeList.get(0)) >= 0 && now.compareTo(storeTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在店内营业时间");
                }
                break;
            //40店内就餐
            case 40:
                if(storeTimeList.size() == 2 && !( now.compareTo(storeTimeList.get(0)) >= 0 && now.compareTo(storeTimeList.get(1)) <= 0 )   ) {
                    throw new BusinessException("不在店内营业时间");
                }
                break;
                default:
                break;
        }

        param.setUserId(user.getUserId());
        Product product = productService.getById(param.getProductId());
        // 商品sku信息
        ProductSkuVo productSku = productUtils.getProductSku(product.getProductId(), product.getSpecType(), param.getProductSkuId());
        // 验证商品能否加入
        this.checkProduct(productSku, product, param.getProductNum());
        UserCart userCart = this.detail(param);
        if (userCart != null) {
            // 更新
            UserCart updateBean = new UserCart();
            updateBean.setCartId(userCart.getCartId());
            updateBean.setProductNum(userCart.getProductNum() + param.getProductNum());
            return this.updateById(updateBean);
        } else {
            // 新增
            UserCart saveBean = new UserCart();
            BeanUtils.copyProperties(param,saveBean);
            saveBean.setDescrib(param.getDescribe());
            return this.save(saveBean);
        }
    }

    /**
     * 验证商品是否可以购买
     * @param productSku
     * @param product
     * @param totalNum
     * @return
     */
    private void checkProduct(ProductSkuVo productSku, Product product, Integer totalNum) {
        // 判断商品是否下架
        if (product == null
                || product.getIsDelete().intValue() == 1
                || product.getProductStatus().intValue() != 10) {
            throw new BusinessException("很抱歉，商品信息不存在或已下架");
        }
        // 判断商品库存
        if (totalNum > productSku.getStockNum()) {
            throw new BusinessException("很抱歉，商品库存不足");
        }
    }


    /**
     * 详情
     * @return
     */
    @Override
    public UserCart detail(UserCartParam param) {
        return this.getOne(new LambdaQueryWrapper<UserCart>()
                .eq(UserCart::getUserId, param.getUserId())
                .eq(UserCart::getProductSkuId, param.getProductSkuId())
                .eq(UserCart::getFeed, param.getFeed())
                .eq(UserCart::getAttr, param.getAttr())
                .eq(UserCart::getProductId, param.getProductId()));
    }

    /**
     * 移除商品
     * @return
     */
    @Override
    public Boolean sub(UserCartParam param) {
        UserCart cart = this.getById(param.getCartId());
        //添加:up, 减少:down
        if("down".equals(param.getType())){
            if (cart.getProductNum().intValue() > 1) {
                // 更新
                UserCart updateBean = new UserCart();
                updateBean.setCartId(cart.getCartId());
                updateBean.setProductNum(cart.getProductNum() - 1);
                return this.updateById(updateBean);
            } else {
                // 删除
                return this.removeById(cart.getCartId());
            }
        }else {
            UserCart updateBean = new UserCart();
            updateBean.setCartId(cart.getCartId());
            updateBean.setProductNum(cart.getProductNum() + 1);
            return this.updateById(updateBean);
        }
    }

    /**
     * 删除商品
     * @return
     */
    @Override
    public Boolean delete(UserCartParam param) {
        return this.remove(new LambdaQueryWrapper<UserCart>()
                .eq(UserCart::getUserId, param.getUserId())
                .eq(UserCart::getShopSupplierId, param.getShopSupplierId())
                .eq(UserCart::getCartType, param.getCartType()));
    }

    /**
     * 购物车商品数量
     * @param user
     * @return
     */
    @Override
    public Integer getTotalCartNum(User user) {
        if(user == null){
            return 0;
        }
        QueryWrapper<UserCart> wrapper = new QueryWrapper<>();
        wrapper.select("sum(product_num) as productNum");
        wrapper.eq("user_id", user.getUserId());
        UserCart cart = this.getOne(wrapper);
        if (cart == null) {
            return 0;
        } else {
            return cart.getProductNum();
        }
    }

    /**
     * 下单后，购物车移除
     * @param cartIds
     */
    @Override
    public void clearAll(String cartIds){
        List<Integer> cartIdList = Arrays.stream(cartIds.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        this.remove(new LambdaQueryWrapper<UserCart>()
                .in(UserCart::getCartId, cartIdList));
    }

    @Override
    public UserCartVo getCartInfo(UserCartParam param) {
        UserCartVo cartVo = userCartMapper.getCartInfo(param);
        if(cartVo == null){
            return null;
        }
        Supplier supplier = supplierService.getById(param.getShopSupplierId());
        if(supplier == null){
            throw new BusinessException("门店不存在");
        }
        //就餐类型,10外卖配送,20外卖自提,30店内打包40店内堂食
        if(param.getDelivery() != null && param.getDelivery() == 40){
            cartVo.setTotalBagPrice(BigDecimal.ZERO);
        }else if(param.getDelivery() != null && param.getDelivery() == 30){
            //店内包装费类型0按商品收费1按单收费
            if(supplier.getStorebagType() == 1){
                //店内包装费
                cartVo.setTotalBagPrice(supplier.getStorebagPrice());
            }
        }else {
            //外卖
            //外卖包装费类型0按商品收费1按单收费
            if(supplier.getBagType() == 1){
                //外卖包装费
                cartVo.setTotalBagPrice(supplier.getBagPrice());
            }
        }

        //购物车总价
        cartVo.setTotalPrice(cartVo.getTotalProductPrice());
        if(cartVo.getTotalBagPrice().compareTo(BigDecimal.ZERO) > 0){
            cartVo.setTotalPrice(cartVo.getTotalPrice().add(cartVo.getTotalBagPrice()));
            cartVo.setTotalLineMoney(cartVo.getTotalLineMoney().add(cartVo.getTotalBagPrice()));
        }
        //最低消费差额
        BigDecimal minMoneyDiff = supplier.getMinMoney().subtract(cartVo.getTotalPrice());
        cartVo.setMinMoneyDiff(minMoneyDiff.compareTo(BigDecimal.ZERO) > 0?minMoneyDiff : BigDecimal.ZERO);
        //实际支付金额
        cartVo.setTotalPayPrice(cartVo.getTotalPrice());
        return cartVo;
    }
}
