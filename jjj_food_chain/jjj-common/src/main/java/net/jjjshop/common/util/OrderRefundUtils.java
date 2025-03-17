package net.jjjshop.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.service.user.UserBalanceLogService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.util.wx.WxPayUtils;
import net.jjjshop.common.vo.WxPayResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class OrderRefundUtils {
    @Autowired
    private WxPayUtils wxPayUtils;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private AppService appService;
    /**
     * 执行订单退款
     */
    public Boolean execute(Order order, BigDecimal money)
    {
        // 退款金额，如不指定则默认为订单实付款金额
        if(money == null){
            money = order.getPayPrice();
        }
        int payType = order.getPayType();
        if (payType != OrderPayTypeEnum.BALANCE.getValue().intValue()
                && order.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            if (order.getRefundMoney().compareTo(order.getBalance()) < 0) {
                if(order.getRefundMoney().add(money).compareTo(order.getBalance()) > 0){
                    BigDecimal balance = order.getBalance().subtract(order.getRefundMoney());
                    money = money.subtract(balance);
                    this.balance(order, balance);
                }else{
                    payType = 10;
                }
            }
        }
        // 1.微信支付退款
        if (payType == OrderPayTypeEnum.WECHAT.getValue()) {
            return this.wxpay(order, money);
        }
        // 2.余额支付退款
        if (payType == OrderPayTypeEnum.BALANCE.getValue()) {
            return this.balance(order, money);
        }
        return false;
    }

    /**
     * 微信支付退款
     */
    private Boolean wxpay(Order order, BigDecimal money)
    {
        App app = appService.getById(order.getAppId());
        if(app.getWxPayKind() == 2){
            return this.refundV2(order, money);
        }else{
            return this.refundV3(order, money);
        }

    }

    private boolean refundV2(Order order, BigDecimal money){
        try{
            WxPayResult wxPayResult = wxPayUtils.getConfig(wxPayService, order.getPaySource(), Long.valueOf(order.getAppId()));
            this.wxPayService.switchoverTo(wxPayResult.getMchId(),wxPayResult.getAppId());
            WxPayRefundRequest request = new WxPayRefundRequest();
            request.setTransactionId(order.getTransactionId());
            request.setTotalFee(order.getOnlineMoney().multiply(new BigDecimal(100)).intValue());
            request.setRefundFee(money.multiply(new BigDecimal(100)).intValue());
            request.setOutRefundNo(OrderUtils.geneOrderNo(order.getUserId()));
            request.setRefundDesc("用户申请取消");
            WxPayRefundResult result = this.wxPayService.refund(request);
            log.info("退款结果:{}:{}",result.getReturnCode(),result.getResultCode());
            if("FAIL".equals(result.getReturnCode())){
                log.info("退款失败return:{}",result.getReturnMsg());
                return false;
            }
            if("FAIL".equals(result.getResultCode())){
                log.info("退款失败result:{}",result.getResultCode());
                return false;
            }
            return true;
        }catch (Exception e){
            log.info("微信退款失败,{}", e.getMessage());
            return false;
        }
    }

    private boolean refundV3(Order order, BigDecimal money){
        try{
            WxPayResult wxPayResult = wxPayUtils.getConfig(wxPayService, order.getPaySource(), Long.valueOf(order.getAppId()));
            this.wxPayService.switchoverTo(wxPayResult.getMchId(),wxPayResult.getAppId());
            WxPayRefundV3Request request = new WxPayRefundV3Request();
            request.setTransactionId(order.getTransactionId());
            request.setOutRefundNo(OrderUtils.geneOrderNo(order.getUserId()));
            // 订单总金额
            BigDecimal payPrice = order.getOnlineMoney();
            WxPayRefundV3Request.Amount am = new WxPayRefundV3Request.Amount();
            am.setTotal(payPrice.multiply(new BigDecimal(100)).intValue());
            am.setRefund(money.multiply(new BigDecimal(100)).intValue());
            am.setCurrency("CNY");
            request.setAmount(am);
            request.setReason("用户申请取消");
            WxPayRefundV3Result result = this.wxPayService.refundV3(request);
            log.info("退款结果:{}", result.getStatus());
            if("CLOSED".equals(result.getStatus()) || "ABNORMAL".equals(result.getStatus())){
                log.info("退款失败:{}", order.getOrderNo());
                return false;
            }
            return true;
        }catch (Exception e){
            log.info("微信退款失败,{}", e.getMessage());
            return false;
        }
    }


    /**
     * 余额支付退款
     */
    private boolean balance(Order order, BigDecimal money)
    {
        // 回退用户余额
        userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, order.getUserId())
                .setSql("`balance` = `balance` + " + money));
        // 记录日志
        UserBalanceLog log = new UserBalanceLog();
        log.setUserId(order.getUserId());
        log.setMoney(money);
        log.setScene(BalanceLogEnum.REFUND.getValue());
        log.setDescription(String.format("用户退款：%s", order.getOrderNo()));
        log.setAppId(order.getAppId());
        userBalanceLogService.save(log);
        return true;
    }

    /**
     * 支付宝退款
     */
    private boolean alipay(Order order, BigDecimal money){
        try{
            App app = appService.getById(order.getAppId());
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                    app.getAlipayAppid(),app.getAlipayPrivatekey(),"json","UTF-8",
                    app.getAlipayPublickey(),"RSA2");
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", order.getTradeNo());
            bizContent.put("refund_amount", money);
            bizContent.put("trade_no", order.getTransactionId());
            bizContent.put("out_request_no", OrderUtils.geneOrderNo(order.getUserId()));

            request.setBizContent(bizContent.toString());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            return response.isSuccess();
        }catch (Exception e){
            log.info("支付宝退款失败,{}", e.getMessage());
            return false;
        }
    }

    public String getIsAgreeAttr(Integer value) {
        switch (value) {
            case 0:
                return "进行中";
            case 10:
                return "已同意";
            case 20:
                return "已拒绝";
        }
        return "";
    }

    public String getStateAttr(Integer value) {
        //售后单状态(0进行中 10已拒绝 20已完成 30已取消)
        switch (value) {
            case 0:
                return "进行中";
            case 10:
                return "已拒绝";
            case 20:
                return "已完成";
            case 30:
                return "已取消";
        }
        return "";
    }

    public String getTypeAttr(Integer value) {
        //售后类型(10退货退款 20换货 30退款)
        switch (value) {
            case 10:
                return "退货退款";
            case 20:
                return "换货";
            case 30:
                return "退款";
        }
        return "";
    }

}
