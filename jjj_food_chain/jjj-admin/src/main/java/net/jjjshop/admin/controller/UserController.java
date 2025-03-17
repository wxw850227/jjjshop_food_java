

package net.jjjshop.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.service.AdminUserService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "user", tags = {"user"})
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/renew", method = RequestMethod.POST)
    @OperationLog(name = "renew")
    @ApiOperation(value = "renew", response = String.class)
    public ApiResult<String> renew(String pass,String checkPass) {
        if(adminUserService.renew(pass,checkPass)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
