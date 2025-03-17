

package net.jjjshop.front.controller.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.ProtocolVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "user", tags = {"index"})
@RestController
@RequestMapping("/front/user/userApple")
public class UserAppleController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @OperationLog(name = "policy")
    @ApiOperation(value = "policy", response = String.class)
    public ApiResult<ProtocolVo> policy() {
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.PROTOCOL.getKey(), null);
        ProtocolVo protocolVo = JSONObject.toJavaObject(vo, ProtocolVo.class);
        return ApiResult.ok(protocolVo);
    }
}
