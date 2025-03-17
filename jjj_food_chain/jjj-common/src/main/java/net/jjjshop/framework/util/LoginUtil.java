

package net.jjjshop.framework.util;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.framework.shiro.util.JwtUtil;
import net.jjjshop.framework.shiro.vo.LoginUserRedisVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 获取登录信息工具类
 */
@Slf4j
@Component
public class LoginUtil {

    private static RedisTemplate redisTemplate;

    public LoginUtil(RedisTemplate redisTemplate) {
        LoginUtil.redisTemplate = redisTemplate;
    }


    /**
     * 获取当前登录用户对象
     *
     * @return
     */
    public static LoginUserRedisVo getLoginUserRedisVo() {
        // 获取当前登录用户
        String token = JwtTokenUtil.getToken("");
        String username = JwtUtil.getUsername(token);
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return (LoginUserRedisVo) redisTemplate.opsForValue().get(String.format(CommonRedisKey.USER_LOGIN_USER, username));
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    public static Integer getUserId() {
        LoginUserRedisVo loginUserRedisVo = getLoginUserRedisVo();
        if (loginUserRedisVo == null) {
            return null;
        }
        return loginUserRedisVo.getUserId();
    }

    /**
     * 获取当前登录用户的账号
     *
     * @return
     */
    public static String getUsername() {
        LoginUserRedisVo loginUserRedisVo = getLoginUserRedisVo();
        if (loginUserRedisVo == null) {
            return null;
        }
        return loginUserRedisVo.getMobile();
    }

}
