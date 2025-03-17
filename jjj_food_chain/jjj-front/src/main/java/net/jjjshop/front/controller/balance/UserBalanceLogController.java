package net.jjjshop.front.controller.balance;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.BalanceCashVo;
import net.jjjshop.common.settings.vo.BalanceVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.balance.UserBalanceLogPageParam;
import net.jjjshop.front.service.balance.UserBalanceLogService;
import net.jjjshop.front.vo.balance.UserBalanceLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "balanceLog", tags = {"用户余额记录"})
@RestController
@RequestMapping("/front/balance/log")
public class UserBalanceLogController extends BaseController {

    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index() throws Exception {
        Map<String, Object> map = new HashMap<>();
        User user = this.getUser(true);
        JSONObject balance = settingUtils.getSetting(SettingEnum.BALANCE.getKey(), null);
        BalanceVo balanceVo = balance.toJavaObject(BalanceVo.class);
        map.put("balanceOpen", balanceVo.getIsOpen());
        JSONObject balanceCash = settingUtils.getSetting(SettingEnum.BALANCECASH.getKey(), null);
        BalanceCashVo balanceCashVo = balanceCash.toJavaObject(BalanceCashVo.class);
        map.put("cashOpen", balanceCashVo.getIsOpen());
        map.put("balance", user.getBalance());
        map.put("list", userBalanceLogService.getTop10(user));
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Paging<UserBalanceLogVo>> lists(@Validated @RequestBody UserBalanceLogPageParam userBalanceLogPageParam)  throws Exception {
        return ApiResult.ok(userBalanceLogService.getList(userBalanceLogPageParam,this.getUser(true)));
    }
}
