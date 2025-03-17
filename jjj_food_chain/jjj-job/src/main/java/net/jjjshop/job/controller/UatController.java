

package net.jjjshop.job.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.controller.BaseController;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "uat", tags = {"uat注入ip测试类"})
@RestController
@RequestMapping("/job/uat")
public class UatController extends BaseController {

    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/ip", method = RequestMethod.GET)
    @OperationLog(name = "ip")
    @ApiOperation(value = "设置ip", response = String.class)
    public String ip(String ip) {
        redisTemplate.opsForValue().set(CommonRedisKey.getRedisKey(CommonRedisKey.UAT_IP), ip);
        return "设置成功"+ip;
    }


}
