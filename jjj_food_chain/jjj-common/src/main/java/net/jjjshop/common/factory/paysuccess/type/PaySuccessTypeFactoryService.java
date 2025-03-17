package net.jjjshop.common.factory.paysuccess.type;

import net.jjjshop.common.vo.order.PayDataVo;

/**
 * 支付成功工厂接口类
 */
public abstract class PaySuccessTypeFactoryService {

    /**
     * 支付成功处理
     */
    public abstract Boolean onPaySuccess(String tradeNo, Integer payStatus, Integer payType, PayDataVo payData);

    /**
     * 支付成功处理
     */
    public abstract PaySuccessOrder getOrder(String tradeNo);
}
