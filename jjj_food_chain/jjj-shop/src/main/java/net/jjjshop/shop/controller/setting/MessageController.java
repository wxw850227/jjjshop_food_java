

package net.jjjshop.shop.controller.setting;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import net.jjjshop.common.enums.GetPhoneTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.setting.MessageSettingsParam;
import net.jjjshop.shop.service.settings.MessageService;
import net.jjjshop.shop.vo.setting.MessageVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "message", tags = {"message"})
@RestController
@RequestMapping("/shop/setting/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SettingUtils settingUtils;


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/message/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<MessageVo>> index(Integer messageTo) throws Exception {
        return ApiResult.ok(messageService.getAll(messageTo));
    }

    //获取消息设置
    @RequestMapping(value = "/field", method = RequestMethod.POST)
    @RequiresPermissions("/setting/message/field")
    @OperationLog(name = "field")
    @ApiOperation(value = "field", response = String.class)
    public ApiResult<Map<String, Object>> field(Integer messageId, String messageType) throws Exception {
        return ApiResult.ok(messageService.getField(messageId, messageType));
    }

    //编辑消息设置
    @RequestMapping(value = "/saveSettings", method = RequestMethod.POST)
    @RequiresPermissions("/setting/message/saveSettings")
    @OperationLog(name = "saveSettings")
    @ApiOperation(value = "saveSettings", response = String.class)
    public ApiResult<String> saveSettings(@RequestBody @Validated MessageSettingsParam messageSettingsParam) {
        if (messageService.saveSettings(messageSettingsParam)) {
            return ApiResult.ok(null, "新增成功");
        } else {
            return ApiResult.fail("新增失败");
        }
    }

    //消息设置状态
    @RequestMapping(value = "/updateSettingsStatus", method = RequestMethod.POST)
    @RequiresPermissions("/setting/message/updateSettingsStatus")
    @OperationLog(name = "updateSettingsStatus")
    @ApiOperation(value = "updateSettingsStatus", response = String.class)
    public ApiResult<String> updateSettingsStatus(Integer messageSettingsId, String messageType) throws Exception {
        if (messageService.updateSettingsStatus(messageSettingsId, messageType)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    //获取手机号
    @RequestMapping(value = "/getPhone", method = RequestMethod.GET)
    @RequiresPermissions("/setting/message/getPhone")
    @OperationLog(name = "getPhone")
    @ApiOperation(value = "getPhone", response = String.class)
    public ApiResult<JSONObject> index() throws Exception {
        JSONObject vo = settingUtils.getSetting(SettingEnum.GETPHONE.getKey(), null);
        //获取页面类型
        vo.put("allType", GetPhoneTypeEnum.getList());
        return ApiResult.ok(vo);
    }

    @RequestMapping(value = "/getPhone", method = RequestMethod.POST)
    @RequiresPermissions("/setting/message/getPhone")
    @OperationLog(name = "getPhone")
    @ApiOperation(value = "getPhone", response = String.class)
    public ApiResult<String> index(@RequestBody JSONObject jsonData) throws Exception {
        settingUtils.saveSetting(SettingEnum.GETPHONE.getKey(), jsonData);
        return ApiResult.ok("保存成功");
    }
}
