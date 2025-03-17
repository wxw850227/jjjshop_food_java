package net.jjjshop.front.service.order.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.entity.order.OrderExtract;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.entity.table.Table;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.*;
import net.jjjshop.common.factory.order.checkpay.CheckPayFactory;
import net.jjjshop.common.factory.paysuccess.type.PaySuccessTypeFactory;
import net.jjjshop.common.factory.product.ProductFactory;
import net.jjjshop.common.factory.product.vo.UpdateProductStockVo;
import net.jjjshop.common.service.product.ProductImageService;
import net.jjjshop.common.service.table.TableService;
import net.jjjshop.common.settings.vo.AdvanceVo;
import net.jjjshop.common.settings.vo.SeckillVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.PayUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.common.vo.order.PayDataVo;
import net.jjjshop.common.vo.user.UserAddressVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.LoginUtil;
import net.jjjshop.front.mapper.order.OrderMapper;
import net.jjjshop.front.param.order.OrderCreateParam;
import net.jjjshop.front.param.order.OrderPageParam;
import net.jjjshop.front.param.order.OrderPayParam;
import net.jjjshop.front.service.app.AppService;
import net.jjjshop.front.service.order.*;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserOrderService;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.order.OrderListVo;
import net.jjjshop.front.vo.product.ProductBuyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private PaySuccessTypeFactory paySuccessTypeFactory;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private OrderExtractService orderExtractService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductFactory productFactory;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private CheckPayFactory checkPayFactory;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private AppService appService;
    @Autowired
    private PayUtils payUtils;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TableService tableService;

    /**
     * 创建订单
     * @param params
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOrder(OrderCreateParam params) {
        // 保存订单主表
        Order order = this.addOrder(params);
        // 保存地址
        if (params.getOrderBuyParam().getDelivery().intValue() == DeliveryTypeEnum.WAIMAI.getValue()) {
            // 记录收货地址
            this.saveOrderAddress(params.getOrderData().getAddress(), order.getOrderId());
        } else if (params.getOrderBuyParam().getDelivery().intValue() == DeliveryTypeEnum.ZITI.getValue()) {
            // 记录自提信息
            this.saveOrderExtract(params, order.getOrderId());
        }
        // 保存商品
        this.saveOrderProduct(params, order.getOrderId());
        // 更新商品库存 (针对下单减库存的商品)
        productFactory.getFactory(params.getOrderSource().getSource()).updateProductStock(this.transStockVo(params.getProductList()));
        return order.getOrderId();
    }

    private List<UpdateProductStockVo> transStockVo(List<ProductBuyVo> productList) {
        // 转成vo
        return productList.stream().map(e -> {
            UpdateProductStockVo vo = new UpdateProductStockVo();
            BeanUtils.copyProperties(e, vo);
            vo.setTotalNum(e.getProductNum());
            return vo;
        }).collect(Collectors.toList());
    }


    private Map<String,String> getLastExtract(Integer userId){
        Map<String,String> result = new HashMap<>();
        result.put("linkman", "");
        result.put("phone", "");
        return result;
    }

    /**
     * 保存订单主表
     * @param params
     * @return
     */
    private synchronized Order addOrder(OrderCreateParam params) {
        Order order = new Order();
        BeanUtils.copyProperties(params.getOrderData(), order);
        order.setUserId(params.getUser().getUserId());
        order.setOrderNo(OrderUtils.geneOrderNo(params.getUser().getUserId()));
        order.setTotalPrice(params.getOrderData().getOrderTotalPrice());
        order.setPayPrice(params.getOrderData().getOrderPayPrice());
        order.setBuyerRemark(params.getOrderBuyParam().getRemark()==null? "" : params.getOrderBuyParam().getRemark());
        order.setOrderSource(params.getOrderSource().getSource());
        order.setActivityId(params.getOrderSource().getActivityId());
        order.setIsAgent(params.getSettledRule().getIsAgent() ? 1 : 0);
        order.setExpressPrice(params.getOrderData().getExpressPrice());
        order.setCouponId(params.getOrderData().getCouponId());
        order.setCouponMoney(params.getOrderData().getCouponMoney());
        order.setShopSupplierId(params.getProductList().get(0).getShopSupplierId());
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐
        order.setDeliveryType(params.getOrderData().getDelivery());
        //用餐方式0外卖1店内
        order.setOrderType(params.getOrderBuyParam().getOrderType());
        order.setTableId(params.getOrderBuyParam().getTableId());
        if(order.getTableId() != null && order.getTableId() > 0){
            Table table = tableService.getById(order.getTableId());
            if(table != null){
                order.setTableNo(table.getTableNo());
            }
        }
        //送餐时间
        order.setMealtime(params.getOrderBuyParam().getMealtime());
        order.setBagPrice(params.getOrderData().getOrderBagPrice());
        //设置取餐号
        order.setCallNo(orderUtils.getCallno(order));
        // 结束时间随主订单配置
        JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), null);
        TradeVo tradeVo = JSONObject.toJavaObject(vo, TradeVo.class);
        int closeDays = tradeVo.getCloseDays();
        //自动关闭时间类型,1=天,2=小时,3=分钟
        int closeType = tradeVo.getCloseType();
        if (closeDays != 0) {
            if(closeType == 1){
                order.setPayEndTime(DateUtil.offsetDay(new Date(), closeDays));
            }else if(closeType == 2){
                order.setPayEndTime(DateUtil.offsetHour(new Date(), closeDays));
            }else if(closeType == 3){
                order.setPayEndTime(DateUtil.offsetMinute(new Date(), closeDays));
            }
        }

        // 如果是满减
        if (params.getOrderData().getReduce() != null) {
            order.setFullreduceMoney(params.getOrderData().getReduce().getReducedPrice());
            order.setFullreduceRemark(params.getOrderData().getReduce().getActiveName());
        }
        this.save(order);
        return order;
    }

    /**
     * 保存地址
     * @param addressVo
     * @param orderId
     * @return
     */
    private void saveOrderAddress(UserAddressVo addressVo, Integer orderId) {
        OrderAddress address = new OrderAddress();
        BeanUtils.copyProperties(addressVo, address);
        address.setOrderId(orderId);
        orderAddressService.save(address);
    }

    /**
     * 保存商品
     * @param params
     * @param orderId
     * @return
     */
    private void saveOrderProduct(OrderCreateParam params, Integer orderId) {
        List<OrderProduct> list = new ArrayList<>();
        for (ProductBuyVo vo : params.getProductList()) {
            OrderProduct product = new OrderProduct();
            BeanUtils.copyProperties(vo, product);
            BeanUtils.copyProperties(vo.getSku(), product);
            product.setProductAttr(vo.getDescrib());
            product.setTotalNum(vo.getProductNum());
            product.setUserId(params.getUser().getUserId());
            List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                    .eq(ProductImage::getProductId, vo.getProductId())
                    .orderByAsc(ProductImage::getId));
            product.setImageId(imageList.get(0).getImageId());
            // 记录订单商品来源id
            product.setProductSourceId(vo.getProductSourceId() == null?0:vo.getProductSourceId());
            // 记录订单商品sku来源id
            product.setSkuSourceId(vo.getSkuSourceId() == null?0:vo.getSkuSourceId());
            product.setOrderId(orderId);
            //支付金额不能为负数
            if(product.getTotalPayPrice() != null && product.getTotalPayPrice().compareTo(BigDecimal.ZERO) < 0){
                product.setTotalPayPrice(BigDecimal.ZERO);
            }
            product.setCreateTime(null);
            list.add(product);
        }
        orderProductService.saveBatch(list);
    }

    /**
     * 保存上门自提联系人
     * @param params
     * @param orderId
     */
    private void saveOrderExtract(OrderCreateParam params, Integer orderId) {
        OrderExtract extract = new OrderExtract();
        extract.setOrderId(orderId);
        extract.setUserId(params.getUser().getUserId());
        extract.setLinkman(params.getOrderBuyParam().getLinkman());
        extract.setPhone(params.getOrderBuyParam().getPhone());
        orderExtractService.save(extract);
    }

    /**
     * 用户订单信息
     * @param orderId
     * @param userId
     * @return
     */
    @Override
    public Order getUserOrderDetail(Integer orderId, Integer userId) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderId, orderId)
                .eq(Order::getUserId, userId));
        if (order == null) {
            throw new BusinessException("用户订单信息不存在");
        }
        return order;
    }

    /**
     * 获取支付参数
     * @param orderPayParam
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> orderPay(OrderPayParam orderPayParam, User user) {
        Map<String, Object> result = new HashMap<>();
        Order order = this.getUserOrderDetail(orderPayParam.getOrderId(), user.getUserId());
        if(order != null && !order.getUserId().equals(user.getUserId())){
            order.setUserId(user.getUserId());
        }
        // 判断订单状态
        if (order.getOrderStatus().intValue() != OrderStatusEnum.NORMAL.getValue().intValue()
                || order.getPayStatus().intValue() != OrderPayStatusEnum.PENDING.getValue().intValue()) {
            throw new BusinessException("很抱歉，当前订单不合法，无法支付");
        }
        // 检查是否可以支付
        List<OrderProduct> orderProductList = orderProductService.getProductList(orderPayParam.getOrderId());
        checkPayFactory.getFactory(order.getOrderSource()).checkOrderStatus(orderProductList);
        // 初始化交易
        this.initOrderPay(order);
        // 在线支付金额
        BigDecimal payPrice = BigDecimal.ZERO;
        payPrice = payPrice.add(order.getPayPrice());
        BigDecimal onlineMoney = payPrice;
        // 如果支付金额为0，则强制使用余额
        if (order.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
            orderPayParam.setUseBalance(true);
        }

        // 余额支付标记订单已支付
        if (orderPayParam.getUseBalance()) {
            onlineMoney = this.paymentByBalance(order, user, orderPayParam);
        } else {
            this.update(new LambdaUpdateWrapper<Order>().eq(Order::getOrderId, order.getOrderId())
                    .set(Order::getOnlineMoney, onlineMoney));
        }
        Object payment = null;
        // 如果需要在线支付
        if (onlineMoney.compareTo(BigDecimal.ZERO) > 0) {
            //订单类型
            Integer value = OrderTypeEnum.MASTER.getValue();
            if(orderPayParam.getOrderType() != null){
                value = orderPayParam.getOrderType();
            }
            try {
                // 微信支付
                if(OrderPayTypeEnum.WECHAT.getValue().intValue() == orderPayParam.getPayType().intValue()){
                    payment = payUtils.onPaymentByWechat(user, order.getOrderNo(), order.getTradeNo(), onlineMoney,
                            orderPayParam.getPaySource(), result, value);
                    // 微信支付版本号
                    result.put("wxPayVersion", appService.getById(user.getAppId()).getWxPayKind());
                }else {
                    throw new BusinessException("很抱歉，用户余额不足，无法支付");
                }
            } catch (Exception e) {
                log.info("微信支付异常：", e.getMessage());
                throw new BusinessException("支付失败:" + e.getMessage());
            }
        }
        result.put("orderId", orderPayParam.getOrderId());
        result.put("payType", orderPayParam.getPayType());
        result.put("payment", payment);
        return result;
    }

    /**
     * 初始化交易
     * @param order
     * @return
     */
    private void initOrderPay(Order order) {
        Order initOrder = new Order();
        initOrder.setOrderId(order.getOrderId());
        initOrder.setBalance(BigDecimal.ZERO);
        initOrder.setOnlineMoney(BigDecimal.ZERO);
        // 重新生成交易号
        initOrder.setTradeNo(OrderUtils.geneOrderNo(order.getUserId()));
        this.updateById(initOrder);
        order.setTradeNo(initOrder.getTradeNo());
    }

    /**
     * 余额支付
     * @param order
     * @param user
     * @param orderPayParam
     * @return
     */
    private BigDecimal paymentByBalance(Order order, User user, OrderPayParam orderPayParam) {
        if (user.getBalance().compareTo(order.getPayPrice()) >= 0) {
            orderPayParam.setPayType(OrderPayTypeEnum.BALANCE.getValue());
            this.update(new LambdaUpdateWrapper<Order>().eq(Order::getOrderId, order.getOrderId())
                    .set(Order::getBalance, order.getPayPrice()));
            PayDataVo data = new PayDataVo();
            JSONObject attach = new JSONObject();
            attach.put("paySource", orderPayParam.getPaySource());
            if(orderPayParam.getOrderType() != null){
                data.setOrderType(orderPayParam.getOrderType());
                attach.put("orderType",orderPayParam.getOrderType());
            }
            data.setAttach(attach);
            this.onPaymentByBalance(order.getTradeNo(), data);
            return BigDecimal.ZERO;
        } else {
            BigDecimal onlineMoney = order.getPayPrice().subtract(user.getBalance());
            this.update(new LambdaUpdateWrapper<Order>().eq(Order::getOrderId, order.getOrderId())
                    .set(Order::getBalance, user.getBalance())
                    .set(Order::getOnlineMoney, onlineMoney));
            return onlineMoney;
        }
    }

    /**
     * 余额支付标记订单已支付
     * @param tradeNo
     * @param data
     * @return
     */
    public void onPaymentByBalance(String tradeNo, PayDataVo data) {
        // 发起余额支付
        if(data.getOrderType() == null){
            paySuccessTypeFactory.getFactory(OrderTypeEnum.MASTER.getValue()).onPaySuccess(tradeNo, 10, OrderPayTypeEnum.BALANCE.getValue(), data);
        }else {
            paySuccessTypeFactory.getFactory(data.getOrderType()).onPaySuccess(tradeNo, 10, OrderPayTypeEnum.BALANCE.getValue(), data);
        }
    }

    /**
     * 订单列表
     * @param param
     * @return
     */
    @Override
    public Paging<OrderListVo> getList(OrderPageParam param) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        Integer userId = LoginUtil.getUserId();
        switch (param.getDataType()) {
            case 0:
                break;
            case 1:
                //订单状态10=>进行中，20=>已经取消，30=>已完成
                wrapper.eq(Order::getOrderStatus, 10);
                break;
            case 2:
                //历史订单 已完成
                wrapper.ne(Order::getOrderStatus, 10);
                break;
            default:
                break;
        }
        //门店ID
        if(param.getShopSupplierId() != null && param.getShopSupplierId() > 0){
            wrapper.eq(Order::getShopSupplierId,param.getShopSupplierId());
        }
        //用餐方式0外卖1店内
        if(param.getOrderType() != null){
            wrapper.eq(Order::getOrderType,param.getOrderType());
        }
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐
        if(param.getDeliveryType() != null && param.getDeliveryType() > 0){
            wrapper.eq(Order::getDeliveryType,param.getDeliveryType());
        }
        wrapper.eq(Order::getUserId, userId);
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.orderByDesc(Order::getCreateTime);
        Page page = new PageInfo(param);
        IPage<Order> ipage = this.page(page, wrapper);
        IPage result = ipage.convert(item -> {
            OrderListVo vo = new OrderListVo();
            this.setVo(item, vo);
            return vo;
        });
        return new Paging(result);
    }

    private void setVo(Order order, OrderListVo vo){
        BeanUtils.copyProperties(order, vo);
        //设置各种Text
        vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
        vo.setPayStatusText(OrderPayStatusEnum.getName(vo.getPayStatus()));
        vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
        //10未收货 20已收货
        vo.setReceiptStatusText(vo.getReceiptStatus() == 10 ? "待收货" : "已收货");
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐
        vo.setOrderLabel(vo.getDeliveryType() == 10 ? "外送":vo.getDeliveryType() == 20 ? "自提":vo.getDeliveryType() == 30 ? "打包":"堂食");
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐
        if(order.getDeliveryType() == 10){
            //设置发货状态
            vo.setDeliveryStatusText(vo.getDeliveryStatus() == 10 ? "待配送" : "已配送");
        }else {
            vo.setDeliveryStatusText("进行中");
        }
        //付款状态(10未付款 20已付款)
        if(vo.getPayStatus() == 10){
            vo.setDeliveryStatusText("待支付");
        }
        //订单状态10=>进行中，20=>已经取消，30=>已完成
        if(vo.getOrderStatus() == 20){
            vo.setDeliveryStatusText("已取消");
        }
        //订单状态10=>进行中，20=>已经取消，30=>已完成
        if(vo.getOrderStatus() == 30){
            vo.setDeliveryStatusText("已完成");
        }
        //设置后台修改价格
        if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0) {
            vo.setUpdatePriceSymbol("+");
        } else if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) < 0) {
            vo.setUpdatePriceSymbol("-");
        } else {
            vo.setUpdatePriceSymbol("");
        }
        //设置订单详情中商品信息
        vo.setProduct(orderProductService.getProductVoList(vo.getOrderId()));
        //门店详情
        if(vo.getShopSupplierId() != null && vo.getShopSupplierId() > 0){
            vo.setSupplier(supplierService.getDetailById(vo.getShopSupplierId()));
        }
        //设置订单状态文本
        vo.setOrderStatusText(OrderUtils.getOrderStatusText(order));
        //设置订单来源文本
        vo.setOrderSourceText(OrderSourceEnum.getName(vo.getOrderSource()));
        //设置订单的商品总数
        vo.getProduct().stream().forEach(e -> {
            if (vo.getTotalNum() != null) {
                vo.setTotalNum(vo.getTotalNum() + e.getTotalNum());
            } else {
                vo.setTotalNum(e.getTotalNum());
            }
        });
        App app = appService.getOne(new LambdaQueryWrapper<App>()
                .eq(App::getAppId,order.getAppId()));
        //微信商户号id
        vo.setMchId(app.getMchid());
    }

    /**
     * 用户取消订单
     * @param order
     * @return
     */
    @Override
    public Boolean cancelOrder(Order order) {
        if (order.getDeliveryStatus() == 20) {
            throw new BusinessException("已发货订单不可取消");
        }
        if (order.getOrderStatus() != 10) {
            throw new BusinessException("订单状态不允许取消");
        }
        //订单是否已经付款
        boolean isPay = order.getPayStatus().equals(OrderPayStatusEnum.SUCCESS.getValue());
        if (!isPay) {
            //商品回退库存
            List<OrderProduct> productList = orderProductService.getProductList(order.getOrderId());
            productFactory.getFactory(order.getOrderSource()).backProductStock(this.transStockOrderProductVo(productList), false);
        }
        //更新订单状态
        Order updateBean = new Order();
        updateBean.setOrderId(order.getOrderId());
        order.setOrderStatus(isPay ? OrderStatusEnum.APPLY_CANCEL.getValue() : OrderStatusEnum.CANCELLED.getValue());
        return this.updateById(order);
    }

    /**
     * 用户确认收货
     * @param orderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean receipt(Integer orderId) {
        Order order = this.getById(orderId);
        if (order.getDeliveryStatus() != 20 || order.getReceiptStatus() != 10) {
            throw new BusinessException("该订单不合法");
        }
        Order updateOrder = new Order();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setReceiptStatus(20);
        updateOrder.setReceiptTime(new Date());
        updateOrder.setOrderStatus(30);
        this.updateById(updateOrder);
        //执行订单完成后操作
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return true;

    }


    private List<UpdateProductStockVo> transStockOrderProductVo(List<OrderProduct> productList) {
        // 转成vo
        return productList.stream().map(e -> {
            UpdateProductStockVo vo = new UpdateProductStockVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户已购买商品数量
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public Integer getHasBuyOrderNum(Integer userId, Integer productId) {
        return orderMapper.getHasBuyOrderNum(userId, productId);
    }

    /**
     * 获取活动订单
     * 已付款，未取消
     * @param userId
     * @param productId
     * @return
     */
    @Override
    public Integer getPlusOrderNum(Integer userId, Integer productId, Integer orderSource){
        return orderMapper.getPlusOrderNum(userId, productId, orderSource);
    }

    /**
     * 根据类型查询订单数量
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Integer getCount(Integer userId, String type) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        switch (type) {
            case "all":
                break;
            case "payment":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.PENDING.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                break;
            case "delivery":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.SUCCESS.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 10);
                break;
            case "received":
                wrapper.eq(Order::getPayStatus, OrderPayStatusEnum.SUCCESS.getValue());
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 20);
                wrapper.eq(Order::getReceiptStatus, 10);
                break;
            case "comment":
                wrapper.eq(Order::getIsComment, 0);
                wrapper.eq(Order::getOrderStatus, 30);
                break;
        }
        wrapper.eq(Order::getUserId, userId);
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.ne(Order::getOrderStatus, 20);
        return this.count(wrapper);
    }

    /**
     * 通过订单编号获取订单详情
     * @param orderNo
     * @return
     */
    @Override
    public OrderDetailVo detailByNo(String orderNo) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        OrderDetailVo vo = userOrderService.transOrderDetailVo(order);
        vo.setProduct(orderProductService.getProductVoList(order.getOrderId()));
        return vo;
    }

    /**
     * 通过订单Id获取订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Integer orderId) {
        Order order = this.getById(orderId);
        OrderDetailVo vo = userOrderService.transOrderDetailVo(order);
        vo.setProduct(orderProductService.getProductVoList(order.getOrderId()));
        return vo;
    }

    /**
     * 订单核销
     * @param orderId
     * @param extractClerkId
     * @return
     */
    @Override
    public Boolean verificationOrder(Integer orderId, Integer extractClerkId) {
        Order order = this.getById(orderId);
        if (order.getPayStatus() != 20
                || !order.getDeliveryType().equals(DeliveryTypeEnum.ZITI.getValue())
                || order.getDeliveryStatus() == 20
                || order.getOrderStatus() == 20
                || order.getOrderStatus() == 21
        ) {
            throw new BusinessException("该订单不满足核销条件");
        }
        order.setExtractClerkId(extractClerkId);
        order.setDeliveryStatus(20);
        order.setDeliveryTime(new Date());
        order.setReceiptStatus(20);
        order.setReceiptTime(new Date());
        order.setOrderStatus(30);
        boolean update = this.updateById(order);
        //完成订单完成后流程
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        //如果是微信付款，则调微信自提发货信息录入
        if(order.getPayType() == 20 && "wx".equals(order.getPaySource())){
            JSONObject setting = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), order.getAppId().longValue());
            StoreVo storeVo = setting.toJavaObject(StoreVo.class);
            //是否自动向微信小程序发货
            Boolean isSendWx = storeVo.getIsSendWx();
            if(isSendWx){
                try {
                    orderUtils.sendWxExpress(order);
                } catch (Exception e) {
                    log.info("核销订单" + order.getOrderId() + "微信小程序发货失败:",e);
                }
            }
        }
        return update;
    }

}
