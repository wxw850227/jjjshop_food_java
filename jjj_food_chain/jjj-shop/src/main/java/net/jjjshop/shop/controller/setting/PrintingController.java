package net.jjjshop.shop.controller.setting;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.PrinterVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.service.settings.PrinterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "printing", tags = {"printing"})
@RestController
@RequestMapping("/shop/setting/printing")
public class PrintingController {
    @Autowired
    private PrinterService printerService;

    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index() throws Exception {
        JSONObject vo = settingUtils.getSetting(SettingEnum.PRINTING.getKey(), null);
        Map<String, Object> result = new HashMap<>();
        result.put("printerList", printerService.getAll());
        result.put("setting", vo);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(@RequestBody JSONObject jsonData) throws Exception {
        PrinterVo printerVo = JSONObject.toJavaObject(jsonData, PrinterVo.class);
        //是否开启商户小票打印
        if(printerVo.getRoomOpen() == 1 && (printerVo.getRoomPrinterId() == null || printerVo.getRoomPrinterId() == 0)){
            throw new BusinessException("商户小票打印机不能为空");
        }
        //是否开启顾客小票打印
        if(printerVo.getBuyerOpen() == 1 && (printerVo.getBuyerPrinterId() == null || printerVo.getBuyerPrinterId() == 0)){
            throw new BusinessException("顾客小票打印机不能为空");
        }
        //是否开启厨房小票打印
        if(printerVo.getSellerOpen() == 1 && (printerVo.getSellerPrinterId() == null || printerVo.getSellerPrinterId() == 0)){
            throw new BusinessException("厨房小票打印机不能为空");
        }
        settingUtils.saveSetting(SettingEnum.PRINTING.getKey(), jsonData);
        return ApiResult.ok("保存成功");
    }
}
