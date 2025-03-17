package net.jjjshop.common.util.message;

import cn.binarywang.wx.miniapp.constant.WxMaApiUrlConstants;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.entity.settings.MessageSettings;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.app.AppWxService;
import net.jjjshop.common.service.order.OrderAddressService;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.common.service.settings.MessageService;
import net.jjjshop.common.service.settings.MessageSettingsService;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.HttpUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.vo.order.WxExpressParams;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.jjjshop.framework.util.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MessageUtils {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageSettingsService messageSettingsService;
    @Autowired
    private WxMessageUtils wxMessageUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private SmsMessageUtils smsMessageUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AppWxService appWxService;
    @Autowired
    private SettingUtils settingUtils;

    /**
     * 支付成功消息
     */
    public void payment(Order order) {
        MessageSettings settings = this.getSettings("order_pay_user", order.getAppId());
        if (settings == null) {
            return;
        }
        User user = userService.getById(order.getUserId());
        List<OrderProduct> productList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, order.getOrderId()));
        JSONObject data = new JSONObject();
        // 订单编号
        data.put("order_no", order.getOrderNo());
        String productName = productList.get(0).getProductName();
        if (productList.size() > 1) {
            productName += "等" + productList.size() + "个";
        }
        // 商品名称
        data.put("product_name", productName);
        // 订单金额
        data.put("pay_price", order.getPayPrice());
        // 支付方式
        data.put("pay_type", OrderPayTypeEnum.getName(order.getPayType()));
        // 支付时间
        data.put("pay_time", DateUtil.format(new Date(), "YYYY-MM-dd HH:mm:ss"));
        this.sendMessage(settings, data, user);
    }

    /**
     * 后台发货通知
     */
    @Async
    public void delivery(Order order) {
        MessageSettings settings = this.getSettings("order_delivery_user", order.getAppId());
        if (settings == null) {
            return;
        }
        User user = userService.getById(order.getUserId());
        List<OrderProduct> productList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, order.getOrderId()));
        JSONObject data = new JSONObject();
        // 订单编号
        data.put("order_no", order.getOrderNo());
        String productName = productList.get(0).getProductName();
        if (productList.size() > 1) {
            productName += "等" + productList.size() + "个";
        }
        // 商品名称
        data.put("product_name", productName);
        // 收货人
        OrderAddress address = orderAddressService.getOne(new LambdaQueryWrapper<OrderAddress>()
                .eq(OrderAddress::getOrderId, order.getOrderId()));
        data.put("name", address.getName());
        // 收货地址
        data.put("address", regionService.getById(address.getProvinceId()).getName()
                + regionService.getById(address.getCityId()).getName()
                + regionService.getById(address.getRegionId()).getName()
                + address.getAddress());
        // 物流单号
        data.put("express_no", order.getExpressNo());
        // 发货时间
        data.put("express_time", DateUtil.format(order.getDeliveryTime(), "YYYY-MM-dd HH:mm:ss"));
        this.sendMessage(settings, data, user);
        //如果是微信付款，则调微信自提发货信息录入
        if(order.getPayType() == 20 && "wx".equals(order.getPaySource())){
            JSONObject setting = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), order.getAppId().longValue());
            StoreVo storeVo = setting.toJavaObject(StoreVo.class);
            //是否自动向微信小程序发货
            Boolean isSendWx = storeVo.getIsSendWx();
            if(isSendWx){
                try {
                    this.sendWxExpress(order);
                } catch (Exception e) {
                    log.info("外卖订单" + order.getOrderId() + "微信小程序发货失败:",e);
                }
            }
        }
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
     * 获取设置
     *
     * @param name
     * @return
     */
    private MessageSettings getSettings(String name, Integer appId) {
        Message message = messageService.getOne(new LambdaQueryWrapper<Message>().eq(Message::getMessageEname, name));
        return messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                .eq(MessageSettings::getMessageId, message.getMessageId()).eq(MessageSettings::getAppId, appId));
    }

    /**
     * 获取模板ID
     */
    public List<String> getMessageByNameArr(String platform, String[] messageENameArr){
        List<String> templateArr = new ArrayList<>();
        //只适用于微信
        if("wx".equals(platform)){
            List<Message> list = messageService.list(new LambdaQueryWrapper<Message>()
                    .eq(Message::getIsDelete, false).in(Message::getMessageEname, messageENameArr)
                    .orderByAsc(Message::getSort));
            for(Message message:list){
                MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                        .eq(MessageSettings::getMessageId, message.getMessageId()));
                if(settings != null && settings.getWxStatus() == 1 && StringUtils.isNotBlank(settings.getWxTemplate())){
                    templateArr.add(JSONObject.parseObject(settings.getWxTemplate()).getString("templateId"));
                }
            }
        }
        //只适用于公众号
        if("mp".equals(platform)){
            List<Message> list = messageService.list(new LambdaQueryWrapper<Message>()
                    .eq(Message::getIsDelete, false).in(Message::getMessageEname, messageENameArr)
                    .orderByAsc(Message::getSort));
            for(Message message:list){
                MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                        .eq(MessageSettings::getMessageId, message.getMessageId()));
                if(settings != null && settings.getMpStatus() == 1 && StringUtils.isNotBlank(settings.getMpTemplate())){
                    templateArr.add(JSONObject.parseObject(settings.getMpTemplate()).getString("templateId"));
                }
            }
        }

        return templateArr;
    }

    private void sendMessage(MessageSettings settings, JSONObject data, User user) {
        // 发送小程序订阅消息
        if (settings.getWxStatus() == 1 && StringUtils.isNotBlank(user.getOpenId())) {
            wxMessageUtils.send(data, settings.getWxTemplate(), user.getOpenId(), Long.valueOf(user.getAppId()));
        }
        // 发送短信消息
        if (settings.getSmsStatus() == 1 && StringUtils.isNotBlank(user.getMobile())) {
            smsMessageUtils.send(data, settings.getSmsTemplate(), user.getMobile(), Long.valueOf(user.getAppId()));
        }
    }
}
