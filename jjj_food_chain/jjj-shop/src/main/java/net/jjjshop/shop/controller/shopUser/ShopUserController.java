

package net.jjjshop.shop.controller.shopUser;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.shiro.vo.LoginShopAccessVo;
import net.jjjshop.framework.shiro.vo.LoginShopUserRedisVo;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.shopUser.ShopUserPageParam;
import net.jjjshop.shop.param.shopUser.ShopUserParam;
import net.jjjshop.shop.service.app.AppService;
import net.jjjshop.shop.service.shop.ShopRoleService;
import net.jjjshop.shop.service.shop.ShopUserService;
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
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/shopUser/shopUser")
public class ShopUserController {
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private ShopRoleService shopRoleService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private AppService appService;

    @RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
    @OperationLog(name = "getRoleList")
    @ApiOperation(value = "getRoleList", response = String.class)
    public ApiResult<List<LoginShopAccessVo>> getRoleList() throws Exception{
        LoginShopUserRedisVo loginShopUserRedisVo = ShopLoginUtil.getLoginShopUserRedisVo();
        return ApiResult.ok(loginShopUserRedisVo.getMenus());
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @OperationLog(name = "getUserInfo")
    @ApiOperation(value = "getUserInfo", response = String.class)
    public ApiResult<Map<String,Object>> getUserInfo() throws Exception{
        Map<String,Object> result = new HashMap<String,Object>();
        // 登录用户名
        result.put("username", ShopLoginUtil.getUsername());
        // 版本号
        result.put("version", springBootJjjProperties.getProjectVersion());
        // 商城名称
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        result.put("shop_name", appService.getName());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody ShopUserPageParam params) throws Exception{
        Map<String,Object> result = new HashMap<>();
        result.put("userList", shopUserService.getList(params));
        result.put("roleList", shopRoleService.getList());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Boolean> add(@Validated @RequestBody ShopUserParam shopUserParam) {
        return ApiResult.ok(shopUserService.add(shopUserParam));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Boolean> edit(@Validated @RequestBody ShopUserParam shopUserParam) {
        return ApiResult.ok(shopUserService.edit(shopUserParam));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<Boolean> delete(Integer shopUserId) {
        return ApiResult.ok(shopUserService.setDelete(shopUserId));
    }
}
