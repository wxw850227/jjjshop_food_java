

package net.jjjshop.framework.util;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.framework.shiro.util.JwtUtil;
import net.jjjshop.framework.shiro.vo.LoginShopUserRedisVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 获取登录信息工具类
 */
@Slf4j
@Component
public class ShopLoginUtil {

    private static RedisTemplate redisTemplate;

    public ShopLoginUtil(RedisTemplate redisTemplate) {
        ShopLoginUtil.redisTemplate = redisTemplate;
    }


    /**
     * 获取当前登录用户对象
     *
     * @return
     */
    public static LoginShopUserRedisVo getLoginShopUserRedisVo() {
        // 获取当前登录用户
        String token = JwtTokenUtil.getToken("shop");
        String username = JwtUtil.getUsername(token);
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return (LoginShopUserRedisVo) redisTemplate.opsForValue().get(String.format(CommonRedisKey.SHOP_LOGIN_USER, username));
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    public static Integer getUserId() {
        LoginShopUserRedisVo loginShopUserRedisVo = getLoginShopUserRedisVo();
        if (loginShopUserRedisVo == null) {
            return 0;
        }
        return loginShopUserRedisVo.getShopUserId();
    }

    /**
     * 获取当前登录用户的门店ID
     *
     * @return
     */
    public static Integer getShopSupplierId() {
        LoginShopUserRedisVo loginShopUserRedisVo = getLoginShopUserRedisVo();
        if (loginShopUserRedisVo == null) {
            return 0;
        }
        return loginShopUserRedisVo.getShopSupplierId();
    }

    /**
     * 获取当前登录用户的AppId
     *
     * @return
     */
    public static Integer getAppId() {
        LoginShopUserRedisVo loginShopUserRedisVo = getLoginShopUserRedisVo();
        if (loginShopUserRedisVo == null) {
            return 0;
        }
        return loginShopUserRedisVo.getAppId();
    }

    /**
     * 获取当前登录用户的账号
     *
     * @return
     */
    public static String getUsername() {
        LoginShopUserRedisVo loginShopUserRedisVo = getLoginShopUserRedisVo();
        if (loginShopUserRedisVo == null) {
            return null;
        }
        return loginShopUserRedisVo.getUserName();
    }

}
