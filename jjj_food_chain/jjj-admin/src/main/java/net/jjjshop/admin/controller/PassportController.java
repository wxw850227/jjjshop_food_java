

package net.jjjshop.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.service.AdminUserService;
import net.jjjshop.admin.vo.LoginAdminUserTokenVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.log.annotation.OperationLogIgnore;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Api(value = "login", tags = {"login"})
@RestController
@RequestMapping("/admin/passport")
public class PassportController {

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @OperationLog(name = "login")
    @ApiOperation(value = "login", response = String.class)
    public ApiResult<LoginAdminUserTokenVo> login(@RequestParam String username,@RequestParam String password, HttpServletResponse response) throws IOException {
        log.debug("username..."+username);
        log.debug("password..."+password);
        LoginAdminUserTokenVo user = adminUserService.login(username, password);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName("admin"), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    @PostMapping("/logout")
    @OperationLogIgnore
    public ApiResult<String> logout(HttpServletRequest request) throws Exception {
        adminUserService.logout(request);
        return ApiResult.ok("退出成功");
    }
}
