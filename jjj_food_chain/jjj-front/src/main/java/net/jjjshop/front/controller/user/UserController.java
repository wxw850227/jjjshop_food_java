

package net.jjjshop.front.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "user", tags = {"用户"})
@RestController
@RequestMapping("/front/user/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    @OperationLog(name = "updateInfo")
    @ApiOperation(value = "updateInfo", response = String.class)
    public ApiResult<String> updateInfo(Integer userId, String updateType, String updateValue){
        if(userService.updateInfo(userId, updateType, updateValue)){
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @OperationLog(name = "update")
    @ApiOperation(value = "update", response = String.class)
    public ApiResult<String> update(String avatarUrl, String nickName){
        if(userService.updateUser(avatarUrl, nickName)){
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
