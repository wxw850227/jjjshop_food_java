package net.jjjshop.shop.controller.supplier;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.TxMapVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.supplier.*;
import net.jjjshop.shop.service.supplier.*;
import net.jjjshop.shop.vo.supplier.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/supplier/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/supplier/supplier/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<SupplierVo>> index(@Validated @RequestBody SupplierPageParam supplierPageParam) throws Exception {
        return ApiResult.ok(supplierService.getList(supplierPageParam));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @RequiresPermissions("/supplier/supplier/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer shopSupplierId) {
        Map<String, Object> result = new HashMap<>();
        SupplierVo supplierVo = supplierService.toEdit(shopSupplierId);
        result.put("model",supplierVo);
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.TX_MAP.getKey(), null);
        TxMapVo txMapVo = JSONObject.parseObject(vo.toJSONString(), TxMapVo.class);
        result.put("key", txMapVo.getTxKey());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/supplier/supplier/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody SupplierParam supplierParam) {
        if(supplierService.edit(supplierParam)){
            return ApiResult.ok(null,"编辑成功");
        }else {
            return ApiResult.fail("编辑失败");
        }
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @RequiresPermissions("/supplier/supplier/setting")
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<SupplierVo> setting(Integer shopSupplierId) {
        return ApiResult.ok(supplierService.editDetail(shopSupplierId));
    }

    @RequestMapping(value = "/setting", method = RequestMethod.POST)
    @RequiresPermissions("/supplier/supplier/setting")
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<String> setting(@Validated @RequestBody SupplierSettingParam supplierParam) {
        if(supplierService.setting(supplierParam)){
            return ApiResult.ok(null,"设置成功");
        }else {
            return ApiResult.fail("设置失败");
        }
    }
}
