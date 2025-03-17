

package net.jjjshop.job.scheduled;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderDeliver;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.product.ProductFactory;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.service.order.OrderDeliverService;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.settings.vo.DeliverVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.job.lock.RedisLock;
import net.jjjshop.job.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单定时任务、未付款关闭，自动确认收货，订单结算
 */

@Slf4j
@Component
public class OrderScheduled {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AppService appService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private ProductFactory productFactory;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private OrderDeliverService orderDeliverService;
    @Autowired
    private SupplierService supplierService;
    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ? ")
    @RedisLock(lockPrefix = "order",lockKey = "execute")
    public void execute() throws Exception {
        // 普通订单行为管理
        this.master();
        // 未支付订单自动关闭
        this.close();
        //自动配送订单
        this.sellerDeliver();
    }

    /**
     * 普通订单行为管理
     */
    private void master(){
        // 获取所有应用
        List<App> appList = appService.list(new LambdaQueryWrapper<App>().eq(App::getIsDelete, 0));
        for(App app:appList){
            // 获取商城交易设置
            JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), Long.valueOf(app.getAppId()));
            TradeVo tradeVo = JSONObject.toJavaObject(vo, TradeVo.class);
            // 已支付订单自动核销(桌台订单除外)
            this.receive(tradeVo.getReceiveDays(), app.getAppId());
        }
    }

    /**
     * 已支付订单自动核销
     */
    private void receive(Integer receiveDays, Integer appId){
        // 截止时间
        if (receiveDays < 1) {
            return;
        }
        Date deadlineTime = DateUtil.offsetMinute(new Date(), -receiveDays);
        // 条件
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                //订单状态10=>进行中，20=>已经取消，30=>已完成
                .eq(Order::getOrderStatus, 10)
                .eq(Order::getAppId, appId)
                //店内用处类型10堂食20快餐
                .ne(Order::getEatType, 10)
                //订单类型0外卖1店内
                .eq(Order::getOrderType, 1)
                //付款时间
                .le(Order::getPayTime, deadlineTime));
        // 订单id集
        List<Integer> orderIds = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());

        if (orderIds.size() > 0) {
            // 更新订单收货状态
            orderService.update(new LambdaUpdateWrapper<Order>().in(Order::getOrderId, orderIds)
                    //收货状态(10未收货 20已收货)
                    .set(Order::getReceiptStatus, 20)
                    //订单状态10=>进行中，20=>已经取消，30=>已完成
                    .set(Order::getOrderStatus, 30)
                    //发货状态(10未发货 20已发货)
                    .set(Order::getDeliveryStatus, 20)
                    //配送状态，待接单＝1,待取货＝2,配送中＝3,已完成＝4
                    .set(Order::getDeliverStatus, 4)
                    .set(Order::getReceiptTime, new Date()));
            //批量更新配送状态
            orderDeliverService.update(new LambdaUpdateWrapper<OrderDeliver>().in(OrderDeliver::getOrderId, orderIds)
                    //配送公司(10商家配送20达达30配送员)
                    .eq(OrderDeliver::getDeliverSource, 10)
                    //订单状态(10进行中 20被取消 30已完成)
                    .eq(OrderDeliver::getStatus, 10)
                    //配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5
                    .set(OrderDeliver::getDeliverStatus, 4)
                    //订单状态(10进行中 20被取消 30已完成)
                    .set(OrderDeliver::getStatus, 30)
                    //配送时间
                    .set(OrderDeliver::getDeliverTime, new Date()));
            // 批量处理已完成的订单
            orderUtils.complete(orderList, appId);
        }
        for(Order order : orderList){
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
        }
        // 记录日志
        if(orderIds.size() > 0){
            log.info(String.format("order scheduled receive receiveDays:%s deadlineTime:%s orderIds:%s", receiveDays, deadlineTime, orderIds));
        }
    }

    /**
     * 未支付订单自动关闭
     */
    private void close()
    {
        // 查询截止时间未支付的订单
        List<Order> items = orderService.list(new LambdaQueryWrapper<Order>()
                //付款状态(10未付款 20已付款)
                .eq(Order::getPayStatus, 10)
                //订单状态10=>进行中，20=>已经取消，30=>已完成
                .eq(Order::getOrderStatus, 10)
                .eq(Order::getIsDelete, 0)
                .isNotNull(Order::getPayEndTime)
                .lt(Order::getPayEndTime, new Date()));
        // 订单id集
        List<Integer> orderIds = items.stream().map(Order::getOrderId).collect(Collectors.toList());
        // 取消订单事件
        if (orderIds.size() > 0) {
            for (Order order:items) {
                // 回退商品库存
                productFactory.getFactory(order.getOrderSource()).backProductStock(orderUtils.getOrderProduct(order.getOrderId()), false);
            }
            // 批量更新订单状态为已取消
            orderService.update(new LambdaUpdateWrapper<Order>().in(Order::getOrderId, orderIds).set(Order::getOrderStatus, 20));
        }
        // 记录日志
        if(orderIds.size() > 0){
            log.info(String.format("order scheduled close orderIds:%s", orderIds));
        }
    }

    /**
     * 自动配送订单
     */
    private void sellerDeliver()
    {
        List<Supplier> supplierList = supplierService.list(new LambdaQueryWrapper<Supplier>()
                .eq(Supplier::getIsDelete,0)
                //店铺状态0营业中1停止营业
                .eq(Supplier::getStatus,0)
                //是否禁用0否1是
                .eq(Supplier::getIsRecycle,0)
        );
        List<Integer> deliverOrderId = new ArrayList<>();
        for(Supplier supplier : supplierList){
            // 获取商城配送设置
            JSONObject vo = settingUtils.getSetting(SettingEnum.DELIVER.getKey(), Long.valueOf(supplier.getShopSupplierId()));
            DeliverVo deliverVo = JSONObject.toJavaObject(vo, DeliverVo.class);
            if("local".equals(deliverVo.getDefaults())){
                // 查询未配送的订单
                List<Order> items = orderService.list(new LambdaQueryWrapper<Order>()
                        //付款状态(10未付款 20已付款)
                        .eq(Order::getPayStatus, 20)
                        //订单状态10=>进行中，20=>已经取消，30=>已完成
                        .eq(Order::getOrderStatus, 10)
                        //配送状态，待接单＝1,待取货＝2,配送中＝3,已完成＝4
                        .eq(Order::getDeliverStatus, 0)
                        //订单类型0外卖1店内
                        .eq(Order::getOrderType, 0)
                        //配送方式(10外卖配送 20上门取30打包带走40店内就餐)
                        .eq(Order::getDeliveryType, 10)
                        //发货状态(10未发货 20已发货)
                        .eq(Order::getDeliveryStatus, 10)
                        .eq(Order::getIsDelete, 0)
                        .eq(Order::getShopSupplierId,supplier.getShopSupplierId()));
                // 自动配送订单
                for(Order order : items){
                    //未配送订单自动配送
                    if(orderService.sellerDeliver(order)){
                        deliverOrderId.add(order.getOrderId());
                    }
                }
            }
        }

        // 记录日志
        if(deliverOrderId.size() > 0){
            log.info(String.format("order scheduled sellerDeliver deliverOrderId:%s", deliverOrderId));
        }
    }
}
