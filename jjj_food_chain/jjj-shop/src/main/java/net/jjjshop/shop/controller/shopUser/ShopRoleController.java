package net.jjjshop.shop.controller.shopUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopRole;
import net.jjjshop.common.util.ShopAccessUtils;
import net.jjjshop.common.vo.shop.ShopAccessVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.shopUser.ShopRoleParam;
import net.jjjshop.shop.service.shop.ShopRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/shopUser/shopRole")
public class ShopRoleController {
    @Autowired
    private ShopRoleService shopRoleService;
    @Autowired
    private ShopAccessUtils shopAccessUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<ShopRole>> index() throws Exception{
        return ApiResult.ok(shopRoleService.getList());
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<List<ShopAccessVo>> toAdd() {
        return ApiResult.ok(shopAccessUtils.getShopAll());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Boolean> add(@Validated @RequestBody ShopRoleParam shopRoleParam) {
        return ApiResult.ok(shopRoleService.add(shopRoleParam));
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String,Object>> toEdit(Integer roleId) {
        Map<String,Object> result = new HashMap<>();
        result.put("menu", shopAccessUtils.getShopAll());
        List<Integer> selectMenu = shopRoleService.getSelectList(roleId);
        result.put("selectMenu", selectMenu);
        result.put("model", shopRoleService.getById(roleId));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Boolean> edit(@Validated @RequestBody ShopRoleParam shopRoleParam) {
        return ApiResult.ok(shopRoleService.edit(shopRoleParam));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<Boolean> delete(Integer roleId) {
        return ApiResult.ok(shopRoleService.delete(roleId));
    }
}
