

package net.jjjshop.shop.controller.appSetting;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.service.app.AppWxService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "appWxSetting", tags = {"微信小程序设置"})
@RestController
@RequestMapping("/shop/appSetting/appWx")
public class AppWxController {
    @Autowired
    private AppWxService appWxService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("/appSetting/appWx/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<AppWx> index() throws Exception{
        return ApiResult.ok(appWxService.getOne(new LambdaQueryWrapper<>()));
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/appSetting/appWx/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(String wxappId, String wxappSecret) {
        if(appWxService.edit(wxappId, wxappSecret)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
