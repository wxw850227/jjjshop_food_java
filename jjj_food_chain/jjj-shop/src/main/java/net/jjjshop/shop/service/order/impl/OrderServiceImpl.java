package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.RegionCache;
import net.jjjshop.common.entity.order.*;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.*;
import net.jjjshop.common.factory.product.ProductFactory;
import net.jjjshop.common.mapper.order.OrderMapper;
import net.jjjshop.common.param.order.OrderParam;
import net.jjjshop.common.service.order.OrderExtractService;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.*;
import net.jjjshop.common.util.excel.ExcelUtils;
import net.jjjshop.common.util.message.MessageUtils;
import net.jjjshop.common.vo.order.OrderProductVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.order.*;
import net.jjjshop.shop.service.export.ExportService;
import net.jjjshop.shop.service.order.OrderAddressService;
import net.jjjshop.shop.service.order.OrderProductService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.product.ProductService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.order.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
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
    private UserService userService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private ProductFactory productFactory;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private ExportService exportService;
    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private ProductService productService;
    @Autowired
    private RegionCache regionCache;
    @Autowired
    private OrderRefundUtils orderRefundUtils;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private OrderExtractService extractService;

    /**
     * 订单列表
     * @param orderPageParam
     * @return
     */
    public Paging<OrderVo> getList(OrderPageParam orderPageParam) {
        Page<Order> page = new PageInfo<>(orderPageParam);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transWrapper(wrapper, orderPageParam);
        IPage<Order> iPage = this.page(page, wrapper);
        IPage<OrderVo> resultPage = iPage.convert(this::transVo);
        return new Paging(resultPage);
    }

    public OrderVo transVo(Order item){
        OrderVo vo = new OrderVo();
        BeanUtil.copyProperties(item, vo);
        vo.setUser(userService.getById(vo.getUserId()));
        vo.setProduct(this.getProductList(vo.getOrderId()));
        //发货状态文本
        vo.setDeliveryStatusText(vo.getDeliveryStatus() == 10 ? "待配送" : "已配送");
        //配送方式
        vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
        //订单状态
        vo.setOrderStatusText(OrderUtils.getOrderStatusText(item));
        //订单类型 用餐方式0外卖1店内
        vo.setOrderTypeText(vo.getOrderType() == 0?"外卖订单":"店内订单");
        //支付状态文本
        vo.setPayStatusText(vo.getPayStatus() == 10 ? "未付款" : "已付款");
        //支付方式
        vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
        //订单来源
        vo.setOrderSourceText(OrderSourceEnum.getName(vo.getOrderSource()));
        //收货状态
        vo.setReceiptStatusText(vo.getReceiptStatus() == 10 ? "未收货" : "已收货");
        //如果是快递配送
        if (vo.getDeliveryType().intValue() == DeliveryTypeEnum.WAIMAI.getValue().intValue()) {
            OrderAddressVo orderAddressVo = new OrderAddressVo();
            OrderAddress orderAddress = orderAddressService.getOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, vo.getOrderId()));
            if (orderAddress != null) {
                BeanUtils.copyProperties(orderAddress, orderAddressVo);
            }
            vo.setAddress(orderAddressVo);
        }
        //如果是自提
        if (vo.getDeliveryType().intValue() == DeliveryTypeEnum.ZITI.getValue().intValue()) {
            OrderExtract extract = extractService.getOne(new LambdaQueryWrapper<OrderExtract>()
                    .eq(OrderExtract::getOrderId,vo.getOrderId()));
            vo.setExtract(extract);
        }
        vo.setSupplier(supplierService.getById(vo.getShopSupplierId()));
        vo.setSupplierName(vo.getSupplier().getName());
        return vo;
    }

    /**
     * 获取所有订单
     * @param orderPageParam
     * @return
     */
    private List<OrderVo> getListAll(OrderPageParam orderPageParam) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transWrapper(wrapper, orderPageParam);
        List<OrderVo> result = this.list(wrapper).stream().map(this::transVo).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取订单查询条件
     * @param wrapper
     * @param orderPageParam
     * @return
     */
    public LambdaQueryWrapper<Order> transWrapper(LambdaQueryWrapper<Order> wrapper, OrderPageParam orderPageParam) {
        wrapper = this.transferDataType(wrapper, orderPageParam.getDataType(),orderPageParam);
        return wrapper;
    }


    /**
     * 获取订单内所有商品
     * @param orderId
     * @return
     */
    private List<OrderProductVo> getProductList(Integer orderId) {
        List<OrderProduct> list = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, orderId).orderByAsc(OrderProduct::getOrderProductId));
        return list.stream().map(e -> {
            OrderProductVo vo = new OrderProductVo();
            BeanUtils.copyProperties(e, vo);
            vo.setImagePath(uploadFileUtils.getFilePath(vo.getImageId()));
            vo.setProductNo(productService.getById(vo.getProductId())==null?"":productService.getById(vo.getProductId()).getProductNo());
            return vo;
        }).collect(Collectors.toList());
    }


    /**
     * 取消订单
     * @param orderCancelParam
     * @return
     */
    public Boolean orderCancel(OrderCancelParam orderCancelParam) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderCancelParam.getOrderNo()));
        if (order.getDeliveryStatus() == 20 || order.getOrderStatus() != 10 || order.getPayStatus() != 20) {
            throw new BusinessException("订单不允许取消");
        }
        //执行退款操作
        orderRefundUtils.execute(order, order.getPayPrice());
        //回退商品库存
        productFactory.getFactory(order.getOrderSource()).backProductStock(orderUtils.getOrderProduct(order.getOrderId()), true);
        // 更新订单状态
        order.setOrderStatus(20);
        order.setCancelRemark(orderCancelParam.getCancelRemark());
        return this.updateById(order);
    }

    //订单退款
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public boolean refund(OrderCancelParam param) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, param.getOrderNo()));
        if (order.getOrderStatus() != 10 || order.getPayStatus() != 20) {
            throw new BusinessException("该订单不合法");
        }
        //预售商品退款，待开发
        if (order.getPayPrice().compareTo(param.getRefundMoney().add(order.getRefundMoney())) < 0) {
            throw new BusinessException("退款金额不能大于实际支付金额");
        }
        //执行退款操作
        orderRefundUtils.execute(order, param.getRefundMoney());
        // 更新订单状态
        order.setDeliveryStatus(20);
        order.setDeliveryTime(new Date());
        order.setReceiptStatus(20);
        order.setReceiptTime(new Date());
        order.setOrderStatus(30);
        order.setRefundMoney(param.getRefundMoney().add(order.getRefundMoney()));
        boolean update = this.updateById(order);
        //完成订单完成后流程
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return update;
    }

    //订单配送
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public boolean sendOrder(OrderExtractParam param) {
        Order order = this.getById(param.getOrderId());
        if (order.getOrderStatus() != 10 || order.getDeliverStatus() != 0) {
            throw new BusinessException("订单已发送或已完成");
        }
        //配送
        orderUtils.sendOrder(order);
        // 发送发货通知
        messageUtils.delivery(order);
        return true;
    }

    /**
     * 微信小程序发货录入
     */
    @Override
    public boolean wxDelivery(Integer orderId) {
        Order order = this.getById(orderId);
        if(order == null){
            throw new BusinessException("未找到订单信息");
        }
        //支付来源,mp,wx
        if(!"wx".equals(order.getPaySource())){
            throw new BusinessException("该订单不是微信小程序订单");
        }
        //发货状态(10未发货 20已发货)
        if(order.getWxDeliveryStatus() == 20){
            throw new BusinessException("该订单已发货");
        }
        //订单状态10=>进行中，20=>已经取消，30=>已完成
        if(order.getOrderStatus() == 20){
            throw new BusinessException("订单已取消");
        }
        //支付方式(10余额支付 20微信支付)
        if(order.getPayType() != 20 || StringUtils.isEmpty(order.getTransactionId())){
            throw new BusinessException("该订单不是微信支付订单");
        }
        //微信小程序发货录入
        return orderUtils.sendWxExpress(order);
    }

    /**
     * 审核：用户取消订单
     * @param orderParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmCancel(OrderParam orderParam) {
        Order order = this.getById(orderParam.getOrderId());
        if (order.getOrderStatus() != 21 || order.getPayStatus() != 20) {
            throw new BusinessException("订单不允许取消");
        }
        if (orderParam.getIsCancel()) {
            //回退商品库存
            productFactory.getFactory(order.getOrderSource()).backProductStock(orderUtils.getOrderProduct(order.getOrderId()), true);
        }
        // 更新订单状态
        order.setOrderStatus(orderParam.getIsCancel() ? 20 : 10);
        return this.updateById(order);
    }

    /**
     * 单个订单信息
     * @param orderId
     * @return
     */
    public OrderVo detail(Integer orderId) {
        Order order = this.getById(orderId);
        return transVo(order);
    }

    /**
     * 修改订单价格
     */
    public Boolean updatePrice(OrderPriceParam orderPriceParam){
        Order order = this.getById(orderPriceParam.getOrderId());
        if(order.getPayStatus() != 10){
            throw new BusinessException("该订单不合法");
        }
        if(order.getOrderSource() != 10){
            throw new BusinessException("该订单不合法");
        }
        // 实际付款金额
        BigDecimal payPrice = orderPriceParam.getUpdatePrice().add(orderPriceParam.getUpdateExpressPrice()).setScale(2);
        if(payPrice.compareTo(BigDecimal.ZERO) <=0 ){
            throw new BusinessException("订单实付款价格不能为0.00元");
        }
        order.setOrderNo(OrderUtils.geneOrderNo(order.getUserId()));// 修改订单号, 否则微信支付提示重复
        order.setPayPrice(payPrice);
        order.setOrderPrice(orderPriceParam.getUpdatePrice());
        order.setUpdatePrice(orderPriceParam.getUpdatePrice().subtract(order.getTotalPrice().subtract(order.getCouponMoney())));
        order.setExpressPrice(orderPriceParam.getUpdateExpressPrice());
        return this.updateById(order);
    }

    /**
     * 订单发货
     * @param orderParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delivery(OrderParam orderParam) {
        Order order = this.getById(orderParam.getOrderId());
        return true;
    }

    /**
     * 通过订单状态查询订单数量
     * @param dataType
     * @return
     */
    public Integer getCount(String dataType,OrderPageParam orderPageParam) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transferDataType(wrapper, dataType,orderPageParam);
        return this.count(wrapper);
    }

    /**
     * 获取不同状态的订单
     * @param wrapper
     * @param dataType
     * @return
     */
    private LambdaQueryWrapper<Order> transferDataType(LambdaQueryWrapper<Order> wrapper, String dataType,OrderPageParam orderPageParam) {
        //用餐方式0外卖1店内
        if(orderPageParam.getOrderType() != null){
            wrapper.eq(Order::getOrderType,orderPageParam.getOrderType());
        }
        //门店ID
        if(orderPageParam.getShopSupplierId() != null && orderPageParam.getShopSupplierId() > 0){
            wrapper.eq(Order::getShopSupplierId,orderPageParam.getShopSupplierId());
        }
        //是否删除
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.orderByDesc(Order::getCreateTime);
        //查询时间
        if(CollectionUtils.isNotEmpty(orderPageParam.getCreateTime()) && orderPageParam.getCreateTime().size() == 2) {
            wrapper.between(Order::getCreateTime, orderPageParam.getCreateTime().get(0)+ " 00:00:00", orderPageParam.getCreateTime().get(1) + " 23:59:59");
        }
        //配送方式,10外卖配送,20外卖自提,30店内打包40店内堂食
        if (orderPageParam.getStyleId() != null && orderPageParam.getStyleId() > 0) {
            wrapper.eq(Order::getDeliveryType, orderPageParam.getStyleId());
        }
        //查询参数：订单号
        if (StringUtils.isNotEmpty(orderPageParam.getOrderNo())) {
            wrapper.like(Order::getOrderNo, orderPageParam.getOrderNo());
        }

        switch (dataType) {
            //所有订单数
            case "all":
                break;
            //未付款订单数
            case "payment":
                //付款状态(10未付款 20已付款)
                wrapper.eq(Order::getPayStatus, 10);
                //订单状态10=>进行中，20=>已经取消，30=>已完成
                wrapper.eq(Order::getOrderStatus, 10);
                break;
            //进行中订单数
            case "process":
                //付款状态(10未付款 20已付款)
                wrapper.eq(Order::getPayStatus, 20);
                wrapper.eq(Order::getOrderStatus, 10);
                break;
            //已完成订单数
            case "complete":
                //付款状态(10未付款 20已付款)
                wrapper.eq(Order::getPayStatus, 20);
                //订单状态10=>进行中，20=>已经取消，30=>已完成
                wrapper.eq(Order::getOrderStatus, 30);
                break;
            //已取消订单数
            case "cancel":
                //订单状态10=>进行中，20=>已经取消，30=>已完成
                wrapper.eq(Order::getOrderStatus, 20);
                break;
        }
        return wrapper;
    }


    /**
     * 获取订单统计数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    //获取订单统计数据
    public BigDecimal getOrderData(String startDate, String endDate, String type,Integer shopSupplierId,Integer orderType) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        //开始查询时间不为空
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(Order::getPayTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        //结束查询时间不为空
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(Order::getPayTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            //如果结束查询时间为空,开始查询时间不为空，就默认设置时间查询区间为开始时间+1天
            Date date = DateUtil.parse(startDate + " 23:59:59");
            wrapper.le(Order::getPayTime, date);
        }
        //门店ID
        if(shopSupplierId != null && shopSupplierId > 0){
            wrapper.eq(Order::getShopSupplierId, shopSupplierId);
        }
        //用餐方式0外卖1店内
        if(orderType != null && orderType >= 0){
            wrapper.eq(Order::getOrderType, orderType);
        }
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.eq(Order::getPayStatus, 20);
        wrapper.ne(Order::getOrderStatus, 20);
        List<Order> orderList = this.list(wrapper);
        //根据查询模式返回不同的数值
        if ("order_total".equals(type)) {
            //查询订单总数
            return new BigDecimal(this.count(wrapper)).setScale(0, RoundingMode.DOWN);
        } else if ("order_total_price".equals(type)) {
            //查询付款订单总额
            BigDecimal result = BigDecimal.ZERO;
            for (Order o : orderList) {
                result = result.add(o.getPayPrice());
            }
            return result;
        } else if ("order_user_total".equals(type)) {
            //查询付款用户数
            List<Integer> idList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
            return new BigDecimal(new HashSet<>(idList).size()).setScale(0, BigDecimal.ROUND_DOWN);
        }else if ("order_refund_money".equals(type)) {
            //退款金额
            BigDecimal result = BigDecimal.ZERO;
            for (Order o : orderList) {
                result = result.add(o.getRefundMoney());
            }
            return result;
        }else if ("order_refund_total".equals(type)) {
            //退款订单数
            wrapper.gt(Order::getRefundMoney,0);
            return new BigDecimal(this.count(wrapper)).setScale(0, RoundingMode.DOWN);
        }else if ("income_price".equals(type)) {
            //预计收入
            BigDecimal result = BigDecimal.ZERO;
            for (Order o : orderList) {
                result = result.add(o.getPayPrice().subtract(o.getRefundMoney()));
            }
            return result;
        }
        return BigDecimal.ZERO.setScale(0, BigDecimal.ROUND_DOWN);
    }

    /**
     * 获取待处理订单总数
     * @param
     * @return
     */
    @Override
    public Integer getReviewOrderTotal(Integer orderType) {
        return this.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getPayStatus, 20)
                .eq(Order::getDeliveryStatus, 10)
                .eq(Order::getOrderType, orderType)
                .eq(Order::getOrderStatus, 10));
    }

    /**
     * 获取提现订单总量
     * @param
     * @return
     */
    public Integer getCardOrderTotal() {
        return this.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, 1)
                .eq(Order::getDeliveryStatus, 0)
                .eq(Order::getIsDelete, 0));
    }

    /**
     * 导出订单
     * @param orderPageParam
     * @param httpServletResponse
     * @return
     */
    public void exportList(OrderPageParam orderPageParam, HttpServletResponse httpServletResponse) throws Exception {
        List<OrderVo> orderList = this.getListAll(orderPageParam);
        //构建数据
        List<ExportOrderVo> exportLists = orderList.stream().map(this::tranExportsVo).collect(Collectors.toList());
        ExcelUtils.export(httpServletResponse, "外卖订单表", exportLists, ExportOrderVo.class);
    }

    public ExportOrderVo tranExportsVo(OrderVo item){
        ExportOrderVo vo = new ExportOrderVo();
        BeanUtils.copyProperties(item, vo);
        //设置商品信息
        vo.setProduct(exportService.filterProductInfo(item));
        if(item.getUser() != null){
            vo.setNickName(item.getUser().getNickName());
        }
        //设置物流公司名字
        if (item.getAddress() != null) {
            //设置收货人名字
            vo.setAddressName(item.getAddress().getName());
            //设置收货人电话
            vo.setAddressPhone(item.getAddress().getPhone());
            //详细地址
            vo.setDetailRegion(item.getAddress().getDetail());
        }
        return vo;
    }

    /**
     * 确认核销订单
     * @param orderExtractParam
     * @return
     */
    public Boolean verificationOrder(OrderExtractParam orderExtractParam) {
        Order order = this.getById(orderExtractParam.getOrderId());
        if (order.getPayStatus() != 20 || order.getOrderStatus() == 20 || order.getOrderStatus() == 30) {
            throw new BusinessException("该订单不满足核销条件");
        }
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


    /**
     * 获取所有运送方式
     * @param
     * @return
     */
    public List<JSONObject> getDeliveryList() {
        return DeliveryTypeEnum.getDeliveryList();
    }

    /**
     * 获取兑换记录
     * @param
     * @return
     */
    public Paging<ExchangeVo> getExchange(ExchangePageParam exchangePageParam){
        Page<Order> page = new PageInfo(exchangePageParam);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if(exchangePageParam.getOrderStatus()!=null&&exchangePageParam.getOrderStatus()>-1){
            wrapper.eq(Order::getOrderStatus,exchangePageParam.getOrderStatus());
        }
        if(StringUtils.isNotEmpty(exchangePageParam.getNickName())){
            List<Integer> userIds = userService.list(new LambdaQueryWrapper<User>().like(User::getNickName, exchangePageParam.getNickName())).stream().map(User::getUserId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(userIds)){
                wrapper.in(Order::getUserId, userIds);
            }else {
                wrapper.eq(Order::getUserId, -1);
            }
        }
        wrapper.eq(Order::getOrderSource, 20);
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.orderByDesc(Order::getCreateTime);
        IPage<Order> iPage = this.page(page, wrapper);
        IPage<ExchangeVo> result = iPage.convert(item -> {
            ExchangeVo vo = new ExchangeVo();
            BeanUtils.copyProperties(item, vo);
            User user = userService.getById(vo.getUserId());
            vo.setNickName(user.getNickName());
            vo.setAvatarUrl(user.getAvatarUrl());
            vo.setOrderStatusText(OrderUtils.getOrderStatusText(item));
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 虚拟商品发货
     * @param orderVirtualParam
     * @return
     */
    public boolean virtual(OrderVirtualParam orderVirtualParam) {
        Order order = this.getById(orderVirtualParam.getOrderId());
        if(order.getPayStatus().intValue() != 20
                //|| order.getDeliveryType().intValue() != DeliveryTypeEnum.NO_EXPRESS.getValue()
                || order.getDeliveryStatus().intValue() == 20
                || order.getOrderStatus().intValue() == 20
                || order.getOrderStatus().intValue() == 21){
            throw new BusinessException("该订单不满足发货条件");
        }
        Date now = new Date();
        order.setDeliveryStatus(20);
        order.setDeliveryTime(now);
        order.setReceiptStatus(20);
        order.setReceiptTime(now);
        order.setOrderStatus(30);
        order.setVirtualContent(orderVirtualParam.getVirtualContent());
        this.updateById(order);
        //执行订单完成后操作
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return true;
    }


}
