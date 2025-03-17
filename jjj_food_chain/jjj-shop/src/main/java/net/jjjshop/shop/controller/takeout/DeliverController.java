

package net.jjjshop.shop.controller.takeout;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.DeliverySourceEnum;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.service.order.OrderDeliverService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.statistics.OrderRankingService;
import net.jjjshop.shop.service.statistics.ProductRankingService;
import net.jjjshop.shop.vo.order.OrderDeliverVo;
import net.jjjshop.shop.vo.order.OrderVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "外卖配送管理", tags = {"外卖配送管理"})
@RestController
@RequestMapping("/shop/takeout/deliver")
public class DeliverController {

    @Autowired
    private OrderDeliverService orderDeliverService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/deliver/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody OrderPageParam orderPageParam) throws Exception{
        Map<String,Object> result = new HashMap<>();
        orderPageParam.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        result.put("list", orderDeliverService.getList(orderPageParam));
        //店内配送方式
        result.put("deliverSource", DeliverySourceEnum.getTakeoutList());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/deliver/detail")
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<Map<String,Object>> detail(Integer deliverId) throws Exception{
        return ApiResult.ok(orderDeliverService.detail(deliverId));
    }

    //导出
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @RequiresPermissions("/takeout/deliver/export")
    @OperationLog(name = "export")
    @ApiOperation(value = "export", response = String.class)
    public ApiResult<String> export(OrderPageParam orderPageParam, HttpServletResponse httpServletResponse) throws Exception{
        //用餐方式0外卖1店内
        orderPageParam.setOrderType(0);
        orderPageParam.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        orderDeliverService.exportList(orderPageParam, httpServletResponse);
        return ApiResult.ok(null, "订单导出成功");
    }

    //确认送达
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/deliver/verify")
    @OperationLog(name = "verify")
    @ApiOperation(value = "verify", response = String.class)
    public ApiResult<String> verify(Integer deliverId) throws Exception{
        if(orderDeliverService.verify(deliverId)){
            return ApiResult.ok(null, "操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }

    //取消配送
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/deliver/cancel")
    @OperationLog(name = "cancel")
    @ApiOperation(value = "cancel", response = String.class)
    public ApiResult<String> cancel(Integer deliverId) throws Exception{
        if(orderDeliverService.cancel(deliverId)){
            return ApiResult.ok(null, "操作成功");
        }else {
            return ApiResult.fail("操作失败");
        }
    }
}
