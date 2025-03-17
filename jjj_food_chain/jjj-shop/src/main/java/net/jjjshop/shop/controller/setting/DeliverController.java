package net.jjjshop.shop.controller.setting;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.DeliverVo;
import net.jjjshop.common.settings.vo.PrinterVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.service.settings.PrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "printing", tags = {"同城配送"})
@RestController
@RequestMapping("/shop/setting/deliver")
public class DeliverController {

    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index() throws Exception {
        JSONObject vo = settingUtils.getSetting(SettingEnum.DELIVER.getKey(), null);
        Map<String, Object> result = new HashMap<>();
        result.put("setting", vo);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(@RequestBody JSONObject jsonData) throws Exception {
        DeliverVo deliverVo = JSONObject.toJavaObject(jsonData, DeliverVo.class);
        //商家配送
        if(deliverVo.getDefaults().equals("local")){
            DeliverVo.Local local = JSON.parseObject(
                    JSON.toJSONString(deliverVo.getEngine().get("local")),
                    new TypeReference<DeliverVo.Local>() { });
            if(local.getTime() == null){
                throw new BusinessException("商家自动配送时间不能为空");
            }
        }
        settingUtils.saveSetting(SettingEnum.DELIVER.getKey(), jsonData);
        return ApiResult.ok("保存成功");
    }
}
