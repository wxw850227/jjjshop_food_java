package net.jjjshop.front.util.order;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.OrderSourceEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.table.TableService;
import net.jjjshop.common.settings.vo.PointsVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.order.OrderCreateParam;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.product.ProductCategoryService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.util.order.vo.OrderData;
import net.jjjshop.front.util.order.vo.OrderSource;
import net.jjjshop.front.util.order.vo.SettledRule;
import net.jjjshop.front.vo.product.ProductBuyVo;
import net.jjjshop.front.vo.shop.ShopFullreduceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 订单结算父类
 */
@Slf4j
@Component
@Scope("prototype")
public class OrderSettledUtils {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private ProductCategoryService productCategoryService;

    // 用户信息
    protected User user;
    // 购买商品信息
    protected List<ProductBuyVo> productList;
    // 前端参数
    protected OrderBuyParam orderBuyParam;
    // 订单来源
    protected OrderSource orderSource;
    // 结算规则
    protected SettledRule settledRule;
    // 订单信息
    protected OrderData orderData;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TableService tableService;

    public void init(){
        // 订单来源
        this.orderSource = new OrderSource();
        // 结算规则
        this.settledRule = new SettledRule();
        // 订单信息
        this.orderData = new OrderData();
    }

    public Map<String, Object> settlement(User user, List<ProductBuyVo> productList, OrderBuyParam orderBuyParam){
        this.user = user;
        this.productList = productList;
        this.orderBuyParam = orderBuyParam;
        Map<String, Object> result = new HashMap<>();
        // 订单信息，初始化
        this.getOrderData();
        // 验证商品状态, 是否允许购买
        this.validateProductList();
        // 订单商品总数量
        Integer orderTotalNum = this.productList.stream().mapToInt(ProductBuyVo::getProductNum).sum();
        // 设置订单商品总金额(不含优惠折扣)
        this.setOrderTotalPrice();
        // 订单商品总包装费
        this.setOrderBagPrice(orderBuyParam);
        // 计算订单商品的实际付款金额
        this.setOrderProductPayPrice();

        // 计算订单最终金额
        this.setOrderPayPrice();
        if(orderBuyParam.getTableId() != null && orderBuyParam.getTableId() > 0){
            orderData.setTableNo(tableService.getName(orderBuyParam.getTableId()));
            orderData.setTableId(orderBuyParam.getTableId());
        }
        result.put("orderTotalNum", orderTotalNum);
        result.put("orderInfo", this.orderData);
        result.put("settledRule", this.settledRule);
        result.put("supplier", supplierService.getDetailById(orderBuyParam.getShopSupplierId()));
        return result;
    }

    /**
     * 设置订单商品总包装费
     */
    private void setOrderBagPrice(OrderBuyParam orderBuyParam)
    {
        // 设置订单商品总包装费
        this.orderData.setOrderBagPrice(this.productList.stream().map(ProductBuyVo::getTotalBagPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        Supplier supplier = supplierService.getById(orderBuyParam.getShopSupplierId());
        //就餐类型,10外卖配送,20外卖自提,30店内打包40店内堂食
        if(orderBuyParam.getDelivery() != null && orderBuyParam.getDelivery() == 40){
            this.orderData.setOrderBagPrice(BigDecimal.ZERO);
        }else if(orderBuyParam.getDelivery() != null && orderBuyParam.getDelivery() == 30){
            //店内包装费类型0按商品收费1按单收费
            if(supplier.getStorebagType() == 1){
                //店内包装费
                this.orderData.setOrderBagPrice(supplier.getStorebagPrice());
            }
        }else {
            //外卖
            //外卖包装费类型0按商品收费1按单收费
            if(supplier.getBagType() == 1){
                //外卖包装费
                this.orderData.setOrderBagPrice(supplier.getBagPrice());
            }
        }
    }

    /**
     * 设置最终支付金额
     */
    private void setOrderPayPrice(){
        // 订单金额(含优惠折扣)
        this.orderData.setOrderPrice(this.productList.stream().map(ProductBuyVo::getTotalPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        //订单金额不能为负数
        if(orderData.getOrderPrice() != null && orderData.getOrderPrice().compareTo(BigDecimal.ZERO) < 0){
            orderData.setOrderPrice(BigDecimal.ZERO);
        }
        // 订单实付款金额(订单金额 + 运费 + 包装费)
        this.orderData.setOrderPayPrice(this.orderData.getOrderPrice().add(this.orderData.getExpressPrice())
                .add(this.orderData.getOrderBagPrice()));
    }


    /**
     * 订单基本信息
     */
    private void getOrderData(){
        this.orderData = new OrderData();
        // 配送方式
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        Integer delivery = this.orderBuyParam.getDelivery();
        this.orderData.setDelivery(delivery);
        this.orderData.setDeliverySetting(storeVo.getDeliveryType());
        // 默认地址。
        this.orderData.setExistAddress(this.user.getAddressId() > 0?true:false);
        if(this.user.getAddressId() > 0){
            this.orderData.setAddress(userAddressService.detail(this.user.getAddressId()));
        }
        // 配送费用
        this.orderData.setExpressPrice(BigDecimal.ZERO);
        // 当前用户收货城市是否存在配送规则中
        this.orderData.setIntraRegion(true);
        // 记忆的自提联系方式
        this.orderData.setLastExtract(this.getLastExtract(this.user.getUserId()));
    }

    /**
     * 设置订单的商品总金额(不含优惠折扣)
     */
    private void setOrderTotalPrice()
    {
        // 订单商品的总金额(不含优惠券折扣)
        this.orderData.setOrderTotalPrice(this.productList.stream().map(ProductBuyVo::getTotalProductPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /**
     * 计算订单商品的实际付款金额
     */
    private void setOrderProductPayPrice()
    {
        for(ProductBuyVo product:this.productList){
            BigDecimal value = product.getTotalProductPrice();
            //订单金额不能为负数
            if(value != null && value.compareTo(BigDecimal.ZERO) < 0){
                value = BigDecimal.ZERO;
            }
            product.setTotalPayPrice(value);
        }
    }

    /**
     * 验证商品是否可以购买，子类实现
     */
    protected void validateProductList(){

    }

    /**
     * 创建新订单
     */
    public Integer createOrder(User user, List<ProductBuyVo> productList, OrderBuyParam orderBuyParam)
    {
        // 结算数据
        this.settlement(user, productList, orderBuyParam);
        // 表单验证
        this.validateOrderForm(orderBuyParam);
        // 创建订单
        OrderCreateParam params = new OrderCreateParam();
        params.setOrderBuyParam(orderBuyParam);
        params.setOrderData(orderData);
        params.setSettledRule(settledRule);
        params.setOrderSource(orderSource);
        params.setUser(user);
        params.setProductList(productList);
        return orderService.createOrder(params);
    }

    /**
     * 验证参数
     */
    private void validateOrderForm(OrderBuyParam orderBuyParam){
        if(this.orderData.getDelivery().intValue() == DeliveryTypeEnum.WAIMAI.getValue().intValue()){
            // 外卖配送
            if(this.orderData.getAddress() == null){
                throw new BusinessException("请先选择收货地址");
            }
        }
    }

    private Map<String,String> getLastExtract(Integer userId){
        Map<String,String> result = new HashMap<>();
        result.put("linkman", "");
        result.put("phone", "");
        return result;
    }

    /**
     * 赠送积分
     */
    private void setOrderPointsBonus(){
        // 初始化商品积分赠送数量
        for (ProductBuyVo product:this.productList) {
            product.setPointsBonus(0);
        }
        // 积分设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.POINTS.getKey(), null);
        PointsVo pointsVo = JSONObject.parseObject(vo.toJSONString(), PointsVo.class);
        // 条件：后台开启开启购物送积分
        if (!pointsVo.getIsShoppingDiscount()) {
            return;
        }
        // 设置商品积分赠送数量/先计算总赠送，然后最后一个补余
        // 订单积分赠送数量
        Integer totalPointsBonus = this.orderData.getOrderPrice().multiply(new BigDecimal(pointsVo.getGiftRatio())).divide(new BigDecimal(100)).intValue();
        Integer tempPointsBonus = 0;
        long productCount = this.productList.stream().filter(product -> product.getIsPointsGift() == 1).count();
        long productIndex = 0;
     }

    /**
     * 设置订单优惠券抵扣信息
     */
    private void setOrderFullreduceMoney(ShopFullreduceVo reduce)
    {
        // 计算订单商品优惠券抵扣金额
        FullDeductUtils.setProductFullreduceMoney(this.productList, reduce.getReducedPrice());
    }
}
