package net.jjjshop.front.service.user.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.*;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.front.service.app.AppService;
import net.jjjshop.front.service.order.OrderAddressService;
import net.jjjshop.front.service.order.OrderProductService;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserOrderService;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.order.OrderProductVo;
import net.jjjshop.front.vo.settings.ExpressDetailVo;
import net.jjjshop.front.vo.settings.ExpressListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户订单 服务实现类
 *
 * @author jjjshop
 * @since 2022-08-02
 */

@Slf4j
@Service
public class UserOrderServiceImpl implements UserOrderService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private AppService appService;
    @Autowired
    private SupplierService supplierService;

    /**
     * 获取订单详情
     *
     * @param user
     * @param orderId
     * @return
     */
    public OrderDetailVo detail(User user, Integer orderId) {
        Order order = orderService.getUserOrderDetail(orderId, user.getUserId());
        //剩余支付时间
        String time = "";
        if (order.getPayStatus() == 10 && order.getOrderStatus() != 20 && order.getPayEndTime() != null) {
            time = this.formatPayEndTime(order.getPayEndTime().getTime() - new Date().getTime());
        }
        OrderDetailVo vo = this.transOrderDetailVo(order);
        //获取订单商品信息
        vo.setProduct(orderProductService.getProductVoList(orderId));
        // 是否提示填写表单
        Boolean showTable = false;
        for (OrderProductVo product : vo.getProduct()){
            if(product.getTableId()!=null && product.getTableId()>0){
                showTable=true;
            }
        }
        vo.setShowTable(showTable);
        //设置剩余支付时间
        vo.setPayEndTimeText(time);
        App app = appService.getOne(new LambdaQueryWrapper<App>()
                .eq(App::getAppId,order.getAppId()));
        //微信商户号id
        vo.setMchId(app.getMchid());
        vo.setSupplier(supplierService.getDetailById(order.getShopSupplierId()));

        return vo;
    }

    /**
     * 转换订单详情VO
     *
     * @param order
     * @param
     */
    public OrderDetailVo transOrderDetailVo(Order order) {
        OrderDetailVo vo = new OrderDetailVo();
        BeanUtils.copyProperties(order, vo);
        //订单支付种类文本
        vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
        //订单支付状态文本
        vo.setPayStatusText(OrderPayStatusEnum.getName(vo.getPayStatus()));
        //物流种类文本
        vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
        vo.setReceiptStatusText(vo.getReceiptStatus() == 10 ? "未收货" : "已收货");
        //用餐方式0外卖1店内
        vo.setOrderTypeText(vo.getOrderType() == 0 ? "外卖订单" : "店内订单");
        //配送方式(10外卖配送 20上门取30打包带走40店内就餐
        if(vo.getOrderType() == 0 && vo.getDeliveryType() == 10){
            vo.setAddress(orderAddressService.getOrderAddress(vo.getOrderId()));
        }
        //设置后台修改价格
        if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0) {
            vo.setUpdatePriceSymbol("+");
        } else if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) < 0) {
            vo.setUpdatePriceSymbol("-");
        } else {
            vo.setUpdatePriceSymbol("");
        }
        //设置订单状态文本
        vo.setOrderStatusText(OrderUtils.getOrderStatusText(order));
        //设置订单状态文本
        vo.setStateText(this.getStateTextAttr(order));
        vo.setIsAllowRefund(this.isAllowRefund(order));
        return vo;
    }
    /**
     * 获取订单剩余支付时间
     *
     * @param leftTime
     * @return
     */
    private String formatPayEndTime(Long leftTime) {
        String str = "";
        DecimalFormat df = new DecimalFormat("#");
        if (leftTime <= 0) {
            return "";
        }
        leftTime = leftTime / 1000;
        Double day = Math.floor(leftTime / 86400);
        Double hour = Math.floor((leftTime - day * 86400) / 3600);
        Double min = Math.floor((leftTime - day * 86400 - hour * 3600) / 60);
        if (day > 0) {
            str = str + df.format(day) + "天";
        }
        if (hour > 0) {
            str = str + df.format(hour) + "小时";
        }
        if(min == 0){
            //由于定时任务为每分钟执行，不足一分钟时显示为一分钟
            min = 1.0;
        }
        if (min > 0) {
            str = str + df.format(min) + "分钟";
        }
        return str;
    }

    /**
     * 订单状态文字描述
     *
     * @param order
     * @return
     */
    private String getStateTextAttr(Order order) {
        if (order.getOrderStatus() == 20) {
            return "已取消";
        }
        if (order.getOrderStatus() == 30) {
            return "已完成";
        }
        if (order.getPayStatus() == 10) {
            return "待付款";
        }
        if (order.getDeliveryStatus() == 10) {
            //配送方式(10外卖配送 20上门取30打包带走40店内就餐
            if(order.getDeliveryType() != 10){
                return "进行中";
            }
            return "待配送";
        }
        if (order.getDeliveryStatus() == 20) {
            //配送方式(10外卖配送 20上门取30打包带走40店内就餐
            if(order.getDeliveryType() != 10){
                return "进行中";
            }
            return "配送中";
        }
        return "";
    }

    /**
     * 检查订单是否允许申请售后
     *
     * @param order
     * @return
     */
    private Boolean isAllowRefund(Order order) {
        //必须是已经发货的订单
        if (order.getDeliveryStatus() != 20) {
            return false;
        }
        JSONObject jsonObject = settingUtils.getSetting(SettingEnum.TRADE.getKey(), null);
        TradeVo tradeVo = jsonObject.toJavaObject(TradeVo.class);
        return true;
    }

}
