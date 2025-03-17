

package net.jjjshop.shop.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.param.home.HomeParam;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.service.shop.ShopUserService;
import net.jjjshop.shop.service.statistics.HomeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/index")
public class IndexController {
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private HomeDataService homeDataService;

    @RequestMapping(value = "/base", method = RequestMethod.POST)
    @OperationLog(name = "base")
    @ApiOperation(value = "base", response = String.class)
    public ApiResult<Map<String, String>> base() {
        JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), null);
        Map<String, String> result = new HashMap<String, String>();
        result.put("shopName", vo.getString("shopName"));
        result.put("shopBgImg", vo.getString("shopBgImg"));
        result.put("shopLogoImg", vo.getString("shopLogoImg"));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index(@RequestBody HomeParam param) throws Exception{
        param.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        return ApiResult.ok(homeDataService.getHomeData(param));
    }

    //登录设置
    @RequestMapping(value = "/loginSetting", method = RequestMethod.GET)
    @OperationLog(name = "loginSetting")
    @ApiOperation(value = "loginSetting", response = String.class)
    public ApiResult<JSONObject> loginSetting(){
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        return ApiResult.ok(vo);
    }
}
