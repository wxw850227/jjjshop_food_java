package net.jjjshop.common.factory.paysuccess.source;

import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.factory.printer.PrinterFactory;
import net.jjjshop.common.util.message.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 支付成功工厂接口类
 */
public abstract class PaySuccessSourceFactoryService {
    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private PrinterFactory printerFactory;

    /**
     * 订单公共业务
     */
    protected void onCommonEvent(Order order)
    {
        // 发送消息通知
        messageUtils.payment(order);
        // 小票打印
        printerFactory.print(order);
    }

    /**
     * 支付成功处理
     */
    public abstract void onPaySuccess(Integer orderId);
}
