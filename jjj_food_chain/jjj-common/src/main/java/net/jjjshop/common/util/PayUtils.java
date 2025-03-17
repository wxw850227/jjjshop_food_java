package net.jjjshop.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.util.wx.WxPayUtils;
import net.jjjshop.common.vo.WxPayResult;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@Component
public class PayUtils {

    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayUtils wxPayUtils;
    @Autowired
    private AppService appService;

    /**
     * 微信支付
     * @return
     */
    public Object onPaymentByWechat(User user, String orderNo, String tradeNo, BigDecimal onlineMoney,
                                    String paySource, Map<String, Object> result, Integer orderType) throws Exception{
        App app = appService.getById(user.getAppId());
        if(app.getWxPayKind() == 2){
            return this.onPaymentByWechatV2(user, orderNo, tradeNo, onlineMoney,
                    paySource, result, orderType);
        }else {
            return this.onPaymentByWechatV3(user, orderNo, tradeNo, onlineMoney,
                    paySource, result, orderType);
        }
    }

    public Object onPaymentByWechatV2(User user, String orderNo, String tradeNo, BigDecimal onlineMoney,
                                      String paySource, Map<String, Object> result, Integer orderType) throws Exception{
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setBody(orderNo);
        request.setOutTradeNo(tradeNo);
        request.setTotalFee(onlineMoney.multiply(new BigDecimal(100)).intValue());
        RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
        request.setSpbillCreateIp(requestDetail.getIp());
        request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/wxPay");
        request.setTradeType("JSAPI");
        if("mp".equals(paySource)){
            request.setOpenid(user.getMpopenId());
        }else if("wx".equals(paySource)){
            request.setOpenid(user.getOpenId());
        }else if("android".equals(paySource) || "ios".equals(paySource)){
            request.setOpenid(user.getAppopenId());
            request.setTradeType("APP");
            request.setBody("订单支付");
        }
        if("h5".equals(paySource)){
            request.setTradeType("MWEB");
            //场景信息 必要参数
            JSONObject sceneInfo = new JSONObject();
            sceneInfo.put("type", "Wap");
            sceneInfo.put("wap_url", springBootJjjProperties.getServerIp());
            sceneInfo.put("wap_name", "支付");
            request.setSceneInfo(sceneInfo.toJSONString());
            request.setOpenid("");
            if(orderType.intValue() == OrderTypeEnum.MASTER.getValue().intValue()){
                result.put("returnUrl", URLEncoder.encode(springBootJjjProperties.getServerIp() + "/h5/pages/order/myorder", "UTF-8"));
            }else{
                result.put("returnUrl", URLEncoder.encode(springBootJjjProperties.getServerIp() + "/h5/pages/user/index/index", "UTF-8"));
            }
        }
        JSONObject attach = new JSONObject();
        attach.put("orderType", orderType);
        attach.put("paySource", paySource);
        request.setAttach(attach.toJSONString());
        // 先设置，再调用
        WxPayResult wxPayResult = wxPayUtils.getConfig(wxPayService, paySource, null);
        this.wxPayService.switchover(wxPayResult.getMchId(),wxPayResult.getAppId());
        return this.wxPayService.createOrder(request);
    }


    public Object onPaymentByWechatV3(User user, String orderNo, String tradeNo, BigDecimal onlineMoney,
                                      String paySource, Map<String, Object> result, Integer orderType) throws Exception{
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(tradeNo);
        WxPayUnifiedOrderV3Request.Amount am = new WxPayUnifiedOrderV3Request.Amount();
        am.setTotal(onlineMoney.multiply(new BigDecimal(100)).intValue());
        am.setCurrency("CNY");
        request.setAmount(am);
        RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
        request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/wxPay");
        request.setDescription(tradeNo);

        WxPayUnifiedOrderV3Request.SceneInfo scen = new WxPayUnifiedOrderV3Request.SceneInfo();
        scen.setPayerClientIp(requestDetail.getIp());

        TradeTypeEnum tradeTypeEnum = null;
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        if("mp".equals(paySource)){
            payer.setOpenid(user.getMpopenId());
            tradeTypeEnum = TradeTypeEnum.JSAPI;
        }else if("wx".equals(paySource)){
            payer.setOpenid(user.getOpenId());
            tradeTypeEnum = TradeTypeEnum.JSAPI;
        }else if("android".equals(paySource) || "ios".equals(paySource)){
            payer.setOpenid(user.getAppopenId());
            tradeTypeEnum = TradeTypeEnum.APP;
            request.setDescription("订单支付");
        }
        if("h5".equals(paySource)){
            WxPayUnifiedOrderV3Request.H5Info info = new WxPayUnifiedOrderV3Request.H5Info();
            info.setType("Wap");
            scen.setH5Info(info);
            tradeTypeEnum = TradeTypeEnum.H5;
            if(orderType.intValue() == OrderTypeEnum.MASTER.getValue().intValue()){
                result.put("returnUrl", URLEncoder.encode(springBootJjjProperties.getServerIp() + "/h5/pages/order/myorder", "UTF-8"));
            }else{
                result.put("returnUrl", URLEncoder.encode(springBootJjjProperties.getServerIp() + "/h5/pages/user/index/index", "UTF-8"));
            }
        }
        if(!"h5".equals(paySource)) {
            request.setPayer(payer);
        }
        request.setSceneInfo(scen);
        JSONObject attach = new JSONObject();
        attach.put("orderType", orderType);
        attach.put("paySource", paySource);
        request.setAttach(attach.toJSONString());
        // 先设置，再调用
        WxPayResult wxPayResult =  wxPayUtils.getConfig(wxPayService, paySource, null);
        this.wxPayService.switchover(wxPayResult.getMchId(),wxPayResult.getAppId());
        return this.wxPayService.createOrderV3(tradeTypeEnum, request);
    }
    /**
     * 支付宝支付
     * @return
     */
    public Object onPaymentByAlipay(User user, String tradeNo, BigDecimal onlineMoney,
                                    String paySource, Integer orderType) throws Exception{
        App app = appService.getById(user.getAppId());
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                app.getAlipayAppid(), app.getAlipayPrivatekey(),"json","UTF-8",
                app.getAlipayPublickey(),"RSA2");
        if("android".equals(paySource) || "ios".equals(paySource)){
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("订单支付");
            model.setSubject("订单支付");
            model.setOutTradeNo(tradeNo);
            model.setTotalAmount("" + onlineMoney);
            request.setBizModel(model);
            //异步通知地址
            request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/alipayNotify?orderType="+orderType
                    +"&paySource="+paySource);
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody(); //就是orderString 可以直接给客户端请求，无需再做处理。
        }else{
            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/alipayNotify?orderType="+orderType
                    +"&paySource="+paySource);
            request.setReturnUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/alipayReturn?orderType="+orderType
                    +"&paySource="+paySource);
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", tradeNo);
            bizContent.put("total_amount", onlineMoney);
            bizContent.put("subject", "订单支付");
            bizContent.put("product_code", "QUICK_WAP_WAY");

            request.setBizContent(bizContent.toString());
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
            return response.getBody();
        }
    }
}
