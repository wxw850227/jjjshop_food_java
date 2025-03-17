

package net.jjjshop.front.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.param.order.SignParam;
import net.jjjshop.common.settings.vo.ProtocolVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TxMapVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.util.DateUtil;
import net.jjjshop.front.param.order.CategoryListParam;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.page.PageService;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.vo.user.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/front/index")
public class IndexController extends BaseController {

    @Autowired
    private PageService pageService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UserAddressService userAddressService;


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody SignParam signParam) {
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(false);
        result.put("page", pageService.getPageData(user, signParam.getPageId(),10));
        CategoryListParam param = new CategoryListParam();
        result.put("supplier", supplierService.getDetail(param));
        result.put("user", user);
        List<String> jsApiList = new ArrayList<>();
        jsApiList.add("scanQRCode");
        jsApiList.add("getLocation");
        jsApiList.add("openLocation");
        result.put("jsApiList", jsApiList);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    @OperationLog(name = "nav")
    @ApiOperation(value = "nav", response = String.class)
    public ApiResult<Map<String, Object>> nav() {
        Map<String, Object> result = new HashMap<>();
        result.put("theme", settingUtils.getShopSetting(SettingEnum.THEME.getKey(), null));
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.TX_MAP.getKey(), null);
        TxMapVo txMapVo = JSONObject.parseObject(vo.toJSONString(), TxMapVo.class);
        result.put("key", txMapVo.getTxKey());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @OperationLog(name = "userInfo")
    @ApiOperation(value = "userInfo", response = String.class)
    public ApiResult<UserVo> userInfo(){
        User user = this.getUser(false);
        UserVo vo = new UserVo();
        if(user != null){
            BeanUtils.copyProperties(user,vo);
            if(StringUtils.isEmpty(vo.getAvatarUrl())){
                if(StringUtils.isEmpty(user.getAvatarUrl())){
                    JSONObject setting = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
                    StoreVo storeVo = JSONObject.parseObject(setting.toJSONString(), StoreVo.class);
                    //会员默认头像
                    vo.setAvatarUrl(storeVo.getAvatarUrl());
                }
            }
            vo.setAddressDefault(userAddressService.getById(user.getAddressId()));
        }else {
            vo = null;
        }
        return ApiResult.ok(vo);
    }

    /**
     * 隐私协议
     * @return
     */
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @OperationLog(name = "policy")
    @ApiOperation(value = "policy", response = String.class)
    public ApiResult<ProtocolVo> policy() {
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.PROTOCOL.getKey(), null);
        ProtocolVo protocolVo = JSONObject.toJavaObject(vo, ProtocolVo.class);
        return ApiResult.ok(protocolVo);
    }

    //登录设置
    @RequestMapping(value = "/loginSetting", method = RequestMethod.GET)
    @OperationLog(name = "loginSetting")
    @ApiOperation(value = "loginSetting", response = String.class)
    public ApiResult<StoreVo> loginSetting(){
        JSONObject setting = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = setting.toJavaObject(StoreVo.class);
        return ApiResult.ok(storeVo);
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<Map<String, Object>> setting(){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        JSONObject vo = settingUtils.getShopSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        if(StringUtils.isEmpty(user.getAvatarUrl())){
            //会员默认头像
            user.setAvatarUrl(storeVo.getAvatarUrl());
        }
        result.put("userInfo", user);
        if(user.getBirthday() != null){
            user.setBirthdayText(DateUtil.getDateString(user.getBirthday()));
        }
        return ApiResult.ok(result);
    }
}
