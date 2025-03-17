

package net.jjjshop.shop.controller.appSetting;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.enums.PlatformEnum;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.app.PayParam;
import net.jjjshop.shop.service.app.AppService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "appSetting", tags = {"应用设置"})
@RestController
@RequestMapping("/shop/appSetting/app")
public class AppController {
    @Autowired
    private AppService appService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("/appSetting/app/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<App> index() throws Exception{
        return ApiResult.ok(appService.getOne(new LambdaQueryWrapper<>()));
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/appSetting/app/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(Integer passportType) {
        if(appService.edit(passportType)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    @RequiresPermissions("/appSetting/app/pay")
    @OperationLog(name = "pay")
    @ApiOperation(value = "pay", response = String.class)
    public ApiResult<Map<String, Object>> pay() throws Exception{
        Map<String, Object> result = new HashMap<>();
        result.put("app", appService.detail());
        result.put("payType", OrderPayTypeEnum.getList());
        result.put("platform", PlatformEnum.getList());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @RequiresPermissions("/appSetting/app/pay")
    @OperationLog(name = "pay")
    @ApiOperation(value = "pay", response = String.class)
    public ApiResult<String> pay(@RequestBody @Validated PayParam payParam) {
        if(appService.editPay(payParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/uploadP12", method = RequestMethod.POST)
    @RequiresPermissions("/appSetting/app/pay")
    @OperationLog(name = "uploadP12")
    @ApiOperation(value = "uploadP12", response = String.class)
    public ApiResult<String> uploadP12(@RequestParam("iFile") MultipartFile multipartFile) {
        if(appService.uploadP12(multipartFile)) {
            return ApiResult.ok(null, "上传成功");
        }else{
            return ApiResult.fail("上传失败");
        }
    }
}
