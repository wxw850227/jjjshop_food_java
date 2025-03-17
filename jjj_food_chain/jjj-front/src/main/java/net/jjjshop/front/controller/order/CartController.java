

package net.jjjshop.front.controller.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.product.CartParam;
import net.jjjshop.front.param.product.UserCartParam;
import net.jjjshop.front.service.user.UserCartService;
import net.jjjshop.front.vo.product.ProductUserCartVo;
import net.jjjshop.front.vo.product.UserCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "order", tags = {"购物车管理"})
@RestController
@RequestMapping("/front/order/cart")
public class CartController extends BaseController {
    @Autowired
    private UserCartService userCartService;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Map<String,Object>> lists(@RequestBody UserCartParam param){
        Map<String,Object> map = new HashMap<>();
        User user = this.getUser(true);
        param.setUserId(user.getUserId());
        map.put("cartInfo",userCartService.getCartInfo(param));
        map.put("productList",userCartService.getAll(user,param));
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Map<String,Object>> add(@Validated @RequestBody UserCartParam param){
        Map<String,Object> map = new HashMap<>();
        User user = this.getUser(true);
        param.setUserId(user.getUserId());
        userCartService.add(user, param);
        UserCartVo cartVo = userCartService.getCartInfo(param);
        map.put("cartInfo",cartVo);
        return ApiResult.ok(map,"加入购物车成功");
    }

    //编辑购物车商品数量
    @RequestMapping(value = "/sub", method = RequestMethod.POST)
    @OperationLog(name = "sub")
    @ApiOperation(value = "sub", response = String.class)
    public ApiResult<Map<String,Object>> sub(@RequestBody UserCartParam param){
        Map<String,Object> map = new HashMap<>();
        User user = this.getUser(true);
        param.setUserId(user.getUserId());
        userCartService.sub(param);
        UserCartVo cartVo = userCartService.getCartInfo(param);
        map.put("cartInfo",cartVo);
        return ApiResult.ok(map);
    }

    //编辑分类列表商品数量
    @RequestMapping(value = "/productSub", method = RequestMethod.POST)
    @OperationLog(name = "productSub")
    @ApiOperation(value = "productSub", response = String.class)
    public ApiResult<Map<String,Object>> productSub(@RequestBody UserCartParam param){
        Map<String,Object> map = new HashMap<>();
        User user = this.getUser(true);
        param.setUserId(user.getUserId());
        userCartService.productSub(param);
        map.put("cartInfo",userCartService.getCartInfo(param));
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(@RequestBody UserCartParam param){
        User user = this.getUser(true);
        param.setUserId(user.getUserId());
        if(userCartService.delete(param)){
            return ApiResult.ok(null, "删除购物车商品成功");
        }else{
            return ApiResult.fail("删除购物车商品失败");
        }
    }
}
