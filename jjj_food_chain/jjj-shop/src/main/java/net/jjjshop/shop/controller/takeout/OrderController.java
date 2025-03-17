

package net.jjjshop.shop.controller.takeout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.DeliverVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.vo.order.OrderVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "外卖订单管理", tags = {"外卖订单管理"})
@RestController
@RequestMapping("/shop/takeout/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody OrderPageParam orderPageParam) throws Exception{
        Map<String,Object> result = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        //用餐方式0外卖1店内
        orderPageParam.setOrderType(0);
        orderPageParam.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        //所有订单数
        jsonObject.put("all", orderService.getCount("all",orderPageParam));
        //未付款订单数
        jsonObject.put("payment", orderService.getCount("payment",orderPageParam));
        //进行中订单数
        jsonObject.put("process", orderService.getCount("process",orderPageParam));
        //已完成订单数
        jsonObject.put("complete", orderService.getCount("complete",orderPageParam));
        //已取消订单数
        jsonObject.put("cancel", orderService.getCount("cancel",orderPageParam));
        result.put("list", orderService.getList(orderPageParam));
        result.put("orderCount", jsonObject);
        //店内配送方式
        result.put("exStyle", DeliveryTypeEnum.getTakeoutList());
        // 获取商城配送设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.DELIVER.getKey(), Long.valueOf(ShopLoginUtil.getShopSupplierId()));
        DeliverVo deliverVo = JSONObject.toJavaObject(vo, DeliverVo.class);
        result.put("deliver", deliverVo);
        result.put("deliverName", deliverVo.getEngine().get(deliverVo.getDefaults()));
        JSONObject setting = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = setting.toJavaObject(StoreVo.class);
        //是否向微信发货
        result.put("isSendWx", storeVo.getIsSendWx());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/order/detail")
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<OrderVo> detail(Integer orderId) throws Exception{
        return ApiResult.ok(orderService.detail(orderId));
    }

    /**
     * 微信小程序发货录入
     */
    @RequestMapping(value = "/wxDelivery", method = RequestMethod.POST)
    @RequiresPermissions("/takeout/order/wxDelivery")
    @OperationLog(name = "wxDelivery")
    @ApiOperation(value = "wxDelivery", response = String.class)
    public ApiResult<String> wxDelivery(Integer orderId) throws Exception{
        if (orderService.wxDelivery(orderId)) {
            return ApiResult.ok(null, "发货录入成功");
        } else {
            return ApiResult.fail("发货录入失败");
        }
    }


}
