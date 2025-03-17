package net.jjjshop.front.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.RegionCache;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "settings", tags = {"设置管理"})
@RestController
@RequestMapping("/front/settings")
public class SettingsController extends BaseController {

    @Autowired
    private RegionCache regionCache;
    @Autowired
    private RegionService regionService;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/getRegion", method = RequestMethod.POST)
    @OperationLog(name = "getRegion")
    @ApiOperation(value = "getRegion", response = String.class)
    public ApiResult<List<JSONArray>> getRegion() {
        return ApiResult.ok(regionService.getRegionForApi());
    }

    @RequestMapping(value = "/appShare", method = RequestMethod.GET)
    @OperationLog(name = "appShare")
    @ApiOperation(value = "app分享参数", response = String.class)
    public ApiResult<JSONObject> appShare() {
        return ApiResult.ok(settingUtils.getSetting(SettingEnum.APP_SHARE.getKey(), null));
    }
}
