

package net.jjjshop.front.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenOAuth2ServiceImpl;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.log.annotation.OperationLogIgnore;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.user.PhoneRegisterParam;
import net.jjjshop.front.service.user.UserService;
import net.jjjshop.front.vo.user.LoginUserTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(value = "user", tags = {"index"})
@RestController
@RequestMapping("/front/user/userOpen")
public class UserOpenController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/phoneLogin", method = RequestMethod.POST)
    @OperationLog(name = "phoneLogin")
    @ApiOperation(value = "phoneLogin", response = String.class)
    public ApiResult<LoginUserTokenVo> phoneLogin(@RequestBody PhoneRegisterParam phoneRegisterParam){
        log.debug("phoneLogin...");
        LoginUserTokenVo user = userService.login(phoneRegisterParam.getMobile(), phoneRegisterParam.getPassword());
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName(""), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @OperationLog(name = "register")
    @ApiOperation(value = "register", response = String.class)
    public ApiResult<LoginUserTokenVo> register(@RequestBody PhoneRegisterParam phoneRegisterParam){
        LoginUserTokenVo user = userService.phoneRegister(phoneRegisterParam);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName(""), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    //小程序绑定手机号
    @RequestMapping(value = "/bindMobile", method = RequestMethod.POST)
    @OperationLog(name = "bindMobile")
    @ApiOperation(value = "bindMobile", response = String.class)
    public ApiResult<String> bindMobile(String mobile, String code){
        if(userService.bindMobile(mobile, code)){
            return ApiResult.ok(null, "绑定成功");
        }else {
            return ApiResult.fail("绑定失败");
        }
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
    @OperationLog(name = "resetpassword")
    @ApiOperation(value = "resetpassword", response = String.class)
    public ApiResult<String> resetpassword(String mobile, String code, String password){
        if(userService.renew(mobile, code, password)){
            return ApiResult.ok(null, "修改成功");
        }else {
            return ApiResult.fail("修改失败");
        }
    }

    @PostMapping("/logout")
    @OperationLogIgnore
    public ApiResult<String> logout(HttpServletRequest request) throws Exception {
        userService.logout(request);
        return ApiResult.ok("退出成功");
    }
}
