package net.jjjshop.common.factory.printer;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.entity.order.OrderExtract;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.settings.Printer;
import net.jjjshop.common.entity.table.Table;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.PrinterTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.service.order.OrderAddressService;
import net.jjjshop.common.service.order.OrderExtractService;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.common.service.settings.PrinterService;
import net.jjjshop.common.service.table.TableService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.settings.vo.PrinterVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.util.ShopLoginUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 文件上传工厂
 */
@Slf4j
@Component
public class PrinterFactory {

    private static final String packageName = "net.jjjshop.common.factory.printer.impl.%sPrinterFactoryService";

    @Autowired
    private Map<String, PrinterFactoryService> map;//关键在这个，原理：当一个接口有多个实现类的时候，key为实现类名，value为实现类对象

    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private PrinterService printerService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderExtractService orderExtractService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private AppService appService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;


    public PrinterFactoryService getService(String printerType) {
        String className = String.format(packageName, printerType);
        return map.get(className);
    }

    /**
     * 打印
     * @param order
     */
    public void print(Order order){
        JSONObject vo = settingUtils.getSetting(SettingEnum.PRINTING.getKey(), Long.valueOf(order.getShopSupplierId()));
        PrinterVo printerVo = JSONObject.toJavaObject(vo, PrinterVo.class);
        // 商家打印
        this.sellerPrint(printerVo,order);
        //顾客打印
        this.buyerPrint(printerVo,order);
        // 厨房打印
        this.roomPrint(printerVo,order);
    }

    // 商家打印
    private void sellerPrint(PrinterVo printerVo, Order order) {
        //是否开启商户小票打印
        if(printerVo.getSellerOpen() == 0){
            return;
        }
        Printer printer = printerService.getById(printerVo.getSellerPrinterId());
        if(printer == null || printer.getIsDelete() == 1){
            return;
        }
        // 获取订单打印内容
        String content = this.getPrintContent(order);
        this.getService(PrinterTypeEnum.getClassName(printer.getPrinterType())).printTicket(printer, content);
    }

    // 顾客打印
    private void buyerPrint(PrinterVo printerVo, Order order) {
        //是否开启顾客小票打印
        if(printerVo.getBuyerOpen() == 0){
            return;
        }
        Printer printer = printerService.getById(printerVo.getBuyerPrinterId());
        if(printer == null || printer.getIsDelete() == 1){
            return;
        }
        // 获取订单打印内容
        String content = this.getPrintContent(order);
        this.getService(PrinterTypeEnum.getClassName(printer.getPrinterType())).printTicket(printer, content);
    }

    //厨房打印
    private void roomPrint(PrinterVo printerVo, Order order) {
        //是否开启厨房小票打印
        if(printerVo.getRoomOpen() == 0){
            return;
        }
        Printer printer = printerService.getById(printerVo.getRoomPrinterId());
        if(printer == null || printer.getIsDelete() == 1){
            return;
        }
        // 获取订单打印内容
        String content = this.getPrintContent(order);
        this.getService(PrinterTypeEnum.getClassName(printer.getPrinterType())).printTicket(printer, content);
    }

    /**
     * 构建订单打印的内容
     */
    private String getPrintContent(Order order)
    {
        order = orderService.getById(order.getOrderId());
        App app = appService.getOne(new LambdaQueryWrapper<App>()
                .eq(App::getAppId, order.getAppId()));
        String storeName = app.getAppName();
        OrderAddress orderAddress = orderAddressService.getOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, order.getOrderId()));
        String fullAddress = orderAddressService.getFullAddress(orderAddress);
        User user = userService.getById(order.getUserId());
        OrderExtract extract = orderExtractService.getOne(new LambdaQueryWrapper<OrderExtract>().eq(OrderExtract::getOrderId, order.getOrderId()));
        List<OrderProduct> orderProducts = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, order.getOrderId()));

        StringBuilder content = new StringBuilder();
        content.append("<CB>").append(storeName).append("</CB><BR>");
        content.append("--------------------------------<BR>");
        content.append("昵称").append(user.getNickName()).append(user.getUserId()).append("<BR>");
        content.append("订单号：").append(order.getOrderNo()).append("<BR>");
        if(order.getPayTime() != null){
            content.append("付款时间：").append(DateUtil.format(order.getPayTime(),"YYYY-MM-dd HH:mm:ss")).append("<BR>");
        }else {
            content.append("下单时间：").append(DateUtil.format(order.getCreateTime(),"YYYY-MM-dd HH:mm:ss")).append("<BR>");
        }
        if(order.getDeliveryType().equals(DeliveryTypeEnum.WAIMAI.getValue())){
            content.append("--------------------------------<BR>");
            content.append("收货人：").append(orderAddress.getName()).append("<BR>");
            content.append("联系电话：").append(orderAddress.getPhone()).append("<BR>");
            content.append("收货地址：").append(fullAddress).append("<BR>");
        }
        if(order.getDeliveryType().equals(DeliveryTypeEnum.ZITI.getValue()) && extract != null){
            content.append("--------------------------------<BR>");
            content.append("联系人：").append(extract.getLinkman()).append("<BR>");
            content.append("联系电话：").append(extract.getPhone()).append("<BR>");
//            content.append("自提门店：").append(storeService.getById(order.getExtractStoreId()).getStoreName()).append("<BR>");
        }
        content.append("=========== 商品信息 ===========<BR>");
        for(OrderProduct orderProduct : orderProducts){
            content.append("商品名称：").append(orderProduct.getProductName()).append("<BR>");
            if(StringUtils.isNotBlank(orderProduct.getProductAttr())){
                content.append("商品规格：").append(orderProduct.getProductAttr()).append("<BR>");
            }
            content.append("购买数量：").append(orderProduct.getTotalNum()).append("<BR>");
            content.append("商品总价：").append(orderProduct.getTotalPayPrice()).append("<BR>");
            content.append("--------------------------------<BR>");
        }
        content.append("------------其它费用------------<BR>");
        // 配送费
        if(order.getDeliveryType().equals(DeliveryTypeEnum.WAIMAI.getValue())){
            content.append("[配送费：" +order.getExpressPrice() + "]<BR>");
        }
        //包装费
        if(order.getBagPrice().compareTo(BigDecimal.ZERO) > 0){
            content.append("[包装费：" + order.getBagPrice() + "]<BR>");
        }
        if(StringUtils.isNotBlank(order.getBuyerRemark())){
            content.append("=========== 买家备注 ===========<BR>");
            content.append("<B>").append(order.getBuyerRemark()).append("</B><BR>");
            content.append("--------------------------------<BR>");
        }
        if(order.getCouponMoney().compareTo(BigDecimal.ZERO) > 0){
            content.append("优惠券：").append(order.getCouponMoney()).append("元<BR>");
        }
        if(order.getPointsNum().compareTo(BigDecimal.ZERO) > 0){
            content.append("积分抵扣：").append(order.getPointsNum()).append("元<BR>");
        }
        if(order.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0){
            content.append("后台改价：").append(order.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0 ? "+":"-").append(order.getUpdatePrice()).append("<BR>");
        }
        // 实付款 付款状态(10未付款 20已付款)
        if(order.getPayStatus() == 20){
            content.append("<BOLD><B>实付：￥" + order.getPayPrice() + "</B></BOLD><BR>");
        }else {
            content.append("<BOLD><B>待支付：￥" + order.getPayPrice() + "</B></BOLD><BR>");
        }
        //获取订单桌号
        this.getOrderTableNo(order);
        if(StringUtils.isNotBlank(order.getTableNo())){
            content.append("<CB>桌号：" + order.getTableNo() + "</CB><BR>");
        }
        return content.toString();
    }

    //获取订单桌号
    private void getOrderTableNo(Order vo) {
        if(vo.getTableId() != null && vo.getTableId() > 0){
            Table table = tableService.getById(vo.getTableId());
            if(table != null){
                vo.setTableNo(table.getTableNo());
            }
        }
    }
}
