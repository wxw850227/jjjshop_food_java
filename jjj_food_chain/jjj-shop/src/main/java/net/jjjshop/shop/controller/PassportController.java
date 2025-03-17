

package net.jjjshop.shop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.log.annotation.OperationLogIgnore;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.shop.service.shop.ShopUserService;
import net.jjjshop.framework.shiro.vo.LoginShopUserTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Api(value = "login", tags = {"login"})
@RestController
@RequestMapping("/shop/passport")
public class PassportController {

    @Autowired
    private ShopUserService shopUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @OperationLog(name = "login")
    @ApiOperation(value = "login", response = String.class)
    public ApiResult<LoginShopUserTokenVo> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        log.debug("username..."+username);
        log.debug("password..."+password);
        LoginShopUserTokenVo user = shopUserService.login(username, password);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName("shop"), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    @PostMapping("/editPass")
    @OperationLogIgnore
    public ApiResult<String> editPass(String oldpass, String password, String confirmPass) throws Exception {
        if(shopUserService.renew(oldpass, password, confirmPass)){
            return ApiResult.ok("修改成功");
        }else{
            return ApiResult.ok("修改失败");
        }
    }

    @PostMapping("/logout")
    @OperationLogIgnore
    public ApiResult<String> logout(HttpServletRequest request) throws Exception {
        shopUserService.logout(request);
        return ApiResult.ok("退出成功");
    }
}
