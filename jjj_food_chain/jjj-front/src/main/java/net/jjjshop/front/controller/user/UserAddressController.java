package net.jjjshop.front.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.user.UserAddressParam;
import net.jjjshop.front.service.user.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "user", tags = {"用户地址"})
@RestController
@RequestMapping("/front/user/address")
public class UserAddressController extends BaseController {

    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private RegionService regionService;

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Map<String, Object>> lists(@RequestParam Integer shopSupplierId) {
        return ApiResult.ok(userAddressService.getList(this.getUser(true).getUserId(),shopSupplierId));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody UserAddressParam userAddressParam) {
        if(userAddressService.add(this.getUser(true),userAddressParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<Map<String, Object>> detail(Integer addressId) {
        Map<String, Object> result = new HashMap<>();
        result.put("detail", userAddressService.detail(addressId));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody UserAddressParam userAddressParam) {
        if(userAddressService.edit(userAddressParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    @OperationLog(name = "setDefault")
    @ApiOperation(value = "setDefault", response = String.class)
    public ApiResult setDefault(Integer addressId) {
        return ApiResult.ok(userAddressService.defaultAddress(addressId,this.getUser(true)));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer addressId) {
        if(userAddressService.delById(addressId,this.getUser(true))) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
