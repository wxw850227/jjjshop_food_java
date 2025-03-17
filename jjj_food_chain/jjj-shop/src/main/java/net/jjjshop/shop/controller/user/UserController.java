

package net.jjjshop.shop.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.user.UserPageParam;
import net.jjjshop.shop.param.user.UserParam;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.user.UserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "user", tags = {"user"})
@RestController
@RequestMapping("/shop/user/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/user/user/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@Validated @RequestBody UserPageParam userPageParam) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("userList", userService.getList(userPageParam));
        return ApiResult.ok(result);
    }

    //修改用户余额
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @RequiresPermissions("/user/user/recharge")
    @OperationLog(name = "recharge")
    @ApiOperation(value = "recharge", response = String.class)
    public ApiResult<Boolean> recharge(@Validated @RequestBody UserParam userParam) throws Exception {
        return ApiResult.ok(userService.recharge(userParam));
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/user/user/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(@RequestParam Integer userId) {
        if(userService.setDelete(userId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
