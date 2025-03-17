

package net.jjjshop.shop.controller.setting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.constant.AgentRedisKey;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@Api(value = "clear", tags = {"clear"})
@RestController
@RequestMapping("/shop/setting/clear")
public class ClearController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/clear/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index() {
        String allKey = String.format(CommonRedisKey.SETTING_DATA_ALL, RequestDetailThreadLocal.getRequestDetail().getAppId());
        Set<String> keysSet = redisTemplate.keys(allKey);
        redisTemplate.delete(keysSet);
        // 分销设置
        String allAgentKey = String.format(AgentRedisKey.SETTING_DATA_ALL, RequestDetailThreadLocal.getRequestDetail().getAppId());
        Set<String> agentKeysSet = redisTemplate.keys(allAgentKey);
        redisTemplate.delete(agentKeysSet);
        return ApiResult.ok(null, "清理成功");
    }
}
