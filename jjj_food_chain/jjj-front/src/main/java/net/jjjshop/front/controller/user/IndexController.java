

package net.jjjshop.front.controller.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.page.CenterMenuService;
import net.jjjshop.front.service.page.PageService;
import net.jjjshop.front.service.settings.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "user", tags = {"用户首页"})
@RestController
@RequestMapping("/front/user/index")
public class IndexController extends BaseController {

    @Autowired
    private CenterMenuService centerMenuService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<Map<String, Object>> detail(){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        if(StringUtils.isEmpty(user.getAvatarUrl())){
            //会员默认头像
            user.setAvatarUrl(storeVo.getAvatarUrl());
        }
        result.put("userInfo", user);
        result.put("menus", centerMenuService.getMenus());
        // 是否获取手机号
        result.put("getPhone", messageService.isGetPhone(user));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<Map<String, Object>> setting(){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        if(StringUtils.isEmpty(user.getAvatarUrl())){
            //会员默认头像
            user.setAvatarUrl(storeVo.getAvatarUrl());
        }
        result.put("userInfo", user);
        return ApiResult.ok(result);
    }
}
