package net.jjjshop.common.util;

import cn.binarywang.wx.miniapp.api.WxMaOrderShippingService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.*;
import cn.binarywang.wx.miniapp.bean.shop.response.WxMaOrderShippingInfoBaseResponse;
import cn.binarywang.wx.miniapp.bean.shop.response.WxMaOrderShippingIsTradeManagedResponse;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.entity.order.*;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserAddress;
import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.product.vo.UpdateProductStockVo;
import net.jjjshop.common.service.app.AppWxService;
import net.jjjshop.common.service.order.*;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.service.user.UserAddressService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.wx.AppWxUtils;
import net.jjjshop.common.vo.order.OrderVo;
import net.jjjshop.common.vo.order.WxExpressParams;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.jjjshop.framework.util.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.function.SupplierUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderUtils {
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private AppWxService appWxService;
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private AppWxUtils appWxUtils;
    @Autowired
    private OrderDeliverService orderDeliverService;
    @Autowired
    private UserAddressService userAddressService;
    /**
     * 生成订单号
     * @return
     */
    public static String geneOrderNo(Integer userId){

        String date = DateUtil.format(new Date(), "yyyyMMdd");

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取4位随机数

        //8位用户id
        int subStrLength = 8;
        String sUserId = userId.toString();
        int length = sUserId.length();
        String str;
        if (length >= subStrLength) {
            str = sUserId.substring(length - subStrLength, length);
        } else {
            str = String.format("%0" + subStrLength + "d", userId);
        }

        return date + str + rannum;// 当前时间 + 用户id + 随机数
    }

    /**
     * 设置订单状态
     * @param order
     */
    public static String getOrderStatusText(Order order){
        //订单状态10=>进行中，20=>已经取消，30=>已完成
        if(order.getOrderStatus() == 20){
            return "已取消";
        }
        if(order.getOrderStatus() == 30){
            return "已完成";
        }
        //付款状态(10未付款 20已付款)
        if(order.getPayStatus() == 10){
            return "待付款";
        }
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐
        if(order.getDeliveryType() == 10){
            // 发货状态(10未发货 20已发货)
            if (order.getDeliveryStatus() == 10) {
                return "待配送";
            }
            //收货状态(10未收货 20已收货)
            if (order.getReceiptStatus() == 10) {
                return "配送中";
            }
        }else {
            return "进行中";
        }
        return "";
    }

    /**
     * 通过订单id，查询商品
     * @return
     */
    public List<UpdateProductStockVo> getOrderProduct(Integer orderId){
        List<OrderProduct> list = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, orderId));
        // 转成vo
        return list.stream().map(e -> {
            UpdateProductStockVo vo = new UpdateProductStockVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 订单配送
     */
    public void sendOrder(Order order){
        this.sendLocal(order);
    }

    /**
     * 商家配送
     */
    public void sendLocal(Order order){
        //配送状态，待接单＝1,待取货＝2,配送中＝3,已完成＝4
        order.setDeliverStatus(3);
        //10商家配送20达达30配送员
        order.setDeliverSource(10);
        //发货状态(10未发货 20已发货)
        order.setDeliveryStatus(20);
        orderService.updateById(order);

        Supplier supplier = supplierService.getById(order.getShopSupplierId());
        OrderDeliver orderDeliver = new OrderDeliver();
        BeanUtils.copyProperties(order,orderDeliver);
        //配送公司(10商家配送20达达30配送员)
        orderDeliver.setDeliverSource(10);
        orderDeliver.setPrice(BigDecimal.ZERO);
        //配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)
        orderDeliver.setDeliverStatus(3);
        orderDeliver.setPhone(supplier.getLinkPhone());
        UserAddress address = userAddressService.getById(userService.getById(order.getUserId()).getAddressId());
        if(address != null){
            //配送距离
            orderDeliver.setDistance(getDistance(supplier,address.getLongitude(),address.getLatitude()).intValue());
        }else {
            orderDeliver.setDistance(0);
        }
        orderDeliverService.save(orderDeliver);
    }

    //距离门店距离
    public Double getDistance(Supplier supplier,String longitude,String latitude){
        if(StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
            return 0.0;
        }
        if(StringUtils.isEmpty(supplier.getLongitude()) || StringUtils.isEmpty(supplier.getLatitude())){
            return 0.0;
        }
        //门店坐标经度
        double ulon = Double.parseDouble(supplier.getLongitude());
        //门店坐标纬度
        double ulat = Double.parseDouble(supplier.getLatitude());
        //用户坐标经度
        double slon = Double.parseDouble(longitude);
        //用户坐标纬度
        double slat = Double.parseDouble(latitude);
        GlobalCoordinates source = new GlobalCoordinates(ulon, ulat);
        GlobalCoordinates target = new GlobalCoordinates(slon, slat);
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target);
        return geoCurve.getEllipsoidalDistance();
    }

    /**
     * 订单完成
     */
    public void complete(List<Order> orderList, Integer appId){
        this.settled(orderList);
    }

    /**
     * 执行订单结算
     */
    @Transactional(rollbackFor = Exception.class)
    public void settled(List<Order> orderList)
    {
        // 累积用户实际消费金额
        this.setIncUserData(orderList);
        // 订单id集
        List<Integer> orderIds = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        // 供应商结算
        this.setIncSupplierMoney(orderList);
        // 将订单设置为已结算
        orderService.update(new LambdaUpdateWrapper<Order>().in(Order::getOrderId, orderIds)
                .set(Order::getIsSettled, 1));
    }

    /**
     * 供应商金额=支付金额-运费
     */
    private void setIncSupplierMoney(List<Order> orderList)
    {
        // 累计消费金额
        Map<Integer,BigDecimal> supplierData = new HashMap<>();
        // 计算并累积实际消费金额(需减去售后退款的金额)
        for (Order order:orderList) {
            if(order.getShopSupplierId() == 0 || order.getIsSettled() == 1){
                continue;
            }
            // 供应价格+运费
            BigDecimal supplierMoney = order.getPayPrice().subtract(order.getRefundMoney());
            //线下支付不累积余额
            if(order.getPayType() == 10 || order.getPayType() == 20){
                if(supplierData.get(order.getShopSupplierId()) == null){
                    supplierData.put(order.getShopSupplierId(), supplierMoney);
                }else{
                    supplierData.put(order.getShopSupplierId(), supplierData.get(order.getShopSupplierId()).add(supplierMoney));
                }
            }
        }
        // 累累积供应商结算金额 (批量)
        supplierData.forEach((key, value) -> {
            supplierService.update(new LambdaUpdateWrapper<Supplier>().eq(Supplier::getShopSupplierId, key)
                    .setSql("`total_money` = `total_money` + " + value)
                    .setSql("`money` = `money` + " + value));
        });
    }

    /**
     * 累积用户实际消费金额/赠送积分
     */
    private void setIncUserData(List<Order> orderList)
    {
        // 累计消费金额
        Map<Integer,BigDecimal> expendMoneyData = new HashMap<>();
        // 计算并累积实际消费金额(需减去售后退款的金额)
        for (Order order:orderList) {
            // 订单实际支付金额
            BigDecimal expendMoney = order.getPayPrice();
            if(expendMoney.compareTo(BigDecimal.ZERO) > 0){
                if(expendMoneyData.get(order.getUserId()) == null){
                    expendMoneyData.put(order.getUserId(), expendMoney);
                }else{
                    expendMoneyData.put(order.getUserId(), expendMoneyData.get(order.getUserId()).add(expendMoney));
                }
            }
        }
        // 累积到会员消费金额
        expendMoneyData.forEach((key, value) -> {
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, key)
                    .setSql("`expend_money` = `expend_money` + " + value));
        });
    }

    /**
     * 批量获取订单列表
     */
    public List<OrderVo> getListByIds(List<Integer> orderIds){
        List<Order> list = orderService.list(new LambdaQueryWrapper<Order>().in(Order::getOrderId, orderIds).eq(Order::getIsDelete,0));
        List<OrderVo> voList = list.stream().map(e -> {
            OrderVo vo = new OrderVo();
            BeanUtils.copyProperties(e, vo);
            vo.setProduct(orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                    .eq(OrderProduct::getOrderId, e.getOrderId())));
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }


    /**
     * 获取微信接口调用凭证
     */
    public String getAccessToken(Integer appId) {
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId().intValue();
        }
        AppWx appWx = appWxService.getById(appId);
        if(appWx == null || StringUtils.isEmpty(appWx.getWxappId()) || StringUtils.isEmpty(appWx.getWxappSecret())){
            throw new BusinessException("未设置微信小程序参数");
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appWx.getWxappId() + "&secret=" + appWx.getWxappSecret();
        String loginJson = null;
        try {
            loginJson = HttpUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        cn.hutool.json.JSONObject wxAccessToken =  JSONUtil.parseObj(loginJson);
        return wxAccessToken.getStr("access_token");
    }

    /**
     * 微信小程序发货录入
     */
    public Boolean sendWxExpress(Order order) {
        //微信小程序发货录入Params
        WxExpressParams wxExpressParams = new WxExpressParams();
        JSONObject order_key = new JSONObject();
        //订单单号类型，用于确认需要上传详情的订单。枚举值1，使用下单商户号和商户侧单号；枚举值2，使用微信支付单号。
        order_key.put("order_number_type",2);
        //配配送方式(10外卖配送 20上门取30打包带走40店内就餐)
        if(order.getDeliveryType() != null){
            //自提和虚拟订单异步环境下，微信支付交易号可能未更新，重新查询微信支付交易号
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(order.getOrderId() > 0){
                order = orderService.getById(order.getOrderId());
            }
        }
        order_key.put("transaction_id",order.getTransactionId());
        //订单，需要上传物流信息的订单
        wxExpressParams.setOrder_key(order_key);
        //发货模式，发货模式枚举值：1、UNIFIED_DELIVERY（统一发货）2、SPLIT_DELIVERY（分拆发货） 示例值: UNIFIED_DELIVERY
        wxExpressParams.setDelivery_mode(1);
        JSONArray shipping_list = new JSONArray();
        JSONObject bean = new JSONObject();
        //物流单号，物流快递发货时必填，示例值: 323244567777 字符字节限制: [1, 128]
        String tracking_no = "";
        //物流公司编码，快递公司ID，参见「查询物流公司编码列表」，物流快递发货时必填， 示例值: DHL 字符字节限制: [1, 128]
        String express_company = "";
        //联系方式，当发货的物流公司为顺丰时，联系方式为必填，收件人或寄件人联系方式二选一
        JSONObject contact = new JSONObject();
        String item_desc = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, order.getOrderId()).orderByAsc(OrderProduct::getOrderProductId)).get(0).getProductName();
        bean.put("item_desc",item_desc);
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐50快递发货)
        if(order.getDeliveryType() == 20){
            wxExpressParams.setLogistics_type(4);
        }else if(order.getDeliveryType() == 10){
            //物流模式，发货方式枚举值：1、快递形式 2、同城配送
            wxExpressParams.setLogistics_type(2);
        }else{
            wxExpressParams.setLogistics_type(3);
        }
        shipping_list.add(bean);
        //物流信息列表，发货物流单列表，支持统一发货（单个物流单）和分拆发货（多个物流单）两种模式，多重性: [1, 10]
        wxExpressParams.setShipping_list(shipping_list);
        //上传时间，用于标识请求的先后顺序 示例值: `2022-12-15T13:29:35.120+08:00`
        String upload_time = new DateTime(new Date(), DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))).toString();
        wxExpressParams.setUpload_time(upload_time);
        //支付者，支付者信息
        JSONObject payer = new JSONObject();
        User user = userService.getById(order.getUserId());
        if(StringUtils.isEmpty(user.getOpenId())){
            throw new BusinessException("用户openId不能为空");
        }
        //用户标识，用户在小程序appid下的唯一标识。 下单前需获取到用户的Openid 示例值: oUpF8uMuAJO_M2pxb1Q9zNjWeS6o 字符字节限制: [1, 128]
        payer.put("openid",user.getOpenId());
        wxExpressParams.setPayer(payer);
        //接口调用凭证
        String access_token = getAccessToken(user.getAppId());
        String params = JSONUtil.toJsonStr(wxExpressParams);
        String url = "https://api.weixin.qq.com/wxa/sec/order/upload_shipping_info?access_token="+access_token;
        String resultExpress = null;
        try {
            resultExpress = HttpUtils.doPostJson(url,params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        cn.hutool.json.JSONObject result =  JSONUtil.parseObj(resultExpress);
        if(result.getInt("errcode") != 0){
            log.info("订单发货录入失败,失败原因："+result.getStr("errmsg")+",订单id"+order.getOrderId()+",错误码"+result.getInt("errcode"));
            return false;
        }else {
            if(order.getOrderId() > 0){
                //微信小程序发货录入状态(10未录入 20已录入)
                LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<Order>()
                        .eq(Order::getOrderId,order.getOrderId()).set(Order::getWxDeliveryStatus,20);
                orderService.update(wrapper);
            }
            return true;
        }
    }

    //设置取餐号
    public String getCallno(Order order) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        String call = "A";
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐
        if(order.getDeliveryType() != null && order.getDeliveryType() == 10){
            call = "K";
            wrapper.eq(Order::getDeliveryType,10);
        }else {
            wrapper.ne(Order::getDeliveryType,10);
        }
        Date date = new Date();
        String startTime = net.jjjshop.framework.util.DateUtil.getDateString(date) + " 00:00:00";
        String endTime = net.jjjshop.framework.util.DateUtil.getDateString(date) + " 23:59:59";
        wrapper.between(Order::getCreateTime, startTime, endTime);
        wrapper.eq(Order::getShopSupplierId, order.getShopSupplierId());
        //获取今天的订单数量
        Integer num = orderService.count(wrapper);
        if(num > 0){
            num = num + 1;
        }else {
            num = 1;
        }
        //3位
        int subStrLength = 3;
        String sNum = num.toString();
        int length = sNum.length();
        String str;
        if (length > subStrLength) {
            subStrLength = subStrLength + 1;
        }
        str = String.format("%0" + subStrLength + "d", num);
        return call + str ;
    }
}
