package net.jjjshop.shop.controller.takeout;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.order.OrderCancelParam;
import net.jjjshop.shop.param.order.OrderExtractParam;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.service.order.OrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(value = "外卖订单管理", tags = {"外卖订单管理"})
@RestController
@RequestMapping("/shop/takeout/operate")
public class OrderOperateController {

    @Autowired
    private OrderService orderService;

    //导出
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @RequiresPermissions("/takeout/operate/export")
    @OperationLog(name = "export")
    @ApiOperation(value = "export", response = String.class)
    public ApiResult<String> export(OrderPageParam orderPageParam, HttpServletResponse httpServletResponse) throws Exception{
        //用餐方式0外卖1店内
        orderPageParam.setOrderType(0);
        orderPageParam.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        orderService.exportList(orderPageParam, httpServletResponse);
        return ApiResult.ok(null, "订单导出成功");
    }

    //取消
    @RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/operate/orderCancel")
    @OperationLog(name = "orderCancel")
    @ApiOperation(value = "orderCancel", response = String.class)
    public ApiResult<String> orderCancel(@Validated @RequestBody OrderCancelParam orderCancelParam){
        if (orderService.orderCancel(orderCancelParam)) {
            return ApiResult.ok(null, "取消成功");
        } else {
            return ApiResult.fail("取消失败");
        }
    }

    //订单核销
    @RequestMapping(value = "/extract", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/operate/extract")
    @OperationLog(name = "extract")
    @ApiOperation(value = "extract", response = String.class)
    public ApiResult<String> extract(@Validated @RequestBody OrderExtractParam orderExtractParam) throws Exception{
        if(orderService.verificationOrder(orderExtractParam)){
            return ApiResult.ok(null, "订单核销成功");
        }else {
            return ApiResult.fail("订单核销失败");
        }
    }

    //订单退款
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/operate/refund")
    @OperationLog(name = "refund")
    @ApiOperation(value = "refund", response = String.class)
    public ApiResult<String> refund(@Validated @RequestBody OrderCancelParam param){
        if(orderService.refund(param)){
            return ApiResult.ok(null, "操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }

    //订单配送
    @RequestMapping(value = "/sendOrder", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/operate/sendOrder")
    @OperationLog(name = "sendOrder")
    @ApiOperation(value = "sendOrder", response = String.class)
    public ApiResult<String> sendOrder(@Validated @RequestBody OrderExtractParam param){
        if(orderService.sendOrder(param)){
            return ApiResult.ok(null, "操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }
}
