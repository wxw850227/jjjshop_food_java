

package net.jjjshop.config.constant;

import net.jjjshop.config.properties.SpringBootJjjProperties;

/**
 *  redis key 常量
 **/
public interface CommonRedisKey {

    // --------------------admin端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String ADMIN_LOGIN_TOKEN = "food:admin:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String ADMIN_LOGIN_USER = "food:admin:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String ADMIN_LOGIN_SALT = "food:admin:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String ADMIN_LOGIN_USER_TOKEN = "food:admin:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String ADMIN_LOGIN_USER_ALL_TOKEN = "food:admin:login:user:token:%s:*";

    // --------------------admin端登录信息结束----------------------

    // --------------------shop端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String SHOP_LOGIN_TOKEN = "food:shop:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String SHOP_LOGIN_USER = "food:shop:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String SHOP_LOGIN_SALT = "food:shop:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String SHOP_LOGIN_USER_TOKEN = "food:shop:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String SHOP_LOGIN_USER_ALL_TOKEN = "food:shop:login:user:token:%s:*";

    // --------------------shop端登录信息结束----------------------

    // --------------------supplier端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String SUPPLIER_LOGIN_TOKEN = "food:supplier:login:token:%s";
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String USER_LOGIN_TOKEN = "food:user:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String USER_LOGIN_USER = "food:user:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String USER_LOGIN_SALT = "food:user:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String USER_LOGIN_USER_TOKEN = "food:user:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String USER_LOGIN_USER_ALL_TOKEN = "food:user:login:user:token:%s:*";

    // --------------------front端登录信息结束----------------------
    /**
     * 验证码
     * verify.code:888888
     */
    String VERIFY_CODE = "food:verify.code:%s";

    /**
     * 地区缓存
     */
    String REGION_DATA = "food:region.data";
    /**
     * 设置缓存
     */
    String SETTING_DATA = "food_setting.data:%s:%s";
    /**
     * 设置缓存
     */
    String SUPPLIER_SETTING_DATA = "food_setting.data:%s:%s:%s";
    /**
     * 所有设置缓存
     */
    String SETTING_DATA_ALL = "food_setting.data:%s:*";
    /**
     * 商品分类缓存
     */
    String PRODUCT_CATEGORY_DATA = "food:product.category.data:%s";
    /**
     * uat测试安全ip
     */
    String UAT_IP = "food:uat.ip";
    /**
     * 任务设置缓存
     */
    String TASK_DATA= "food:task.data.%s:%s:%s:%s";


    public static String getRedisKey(String key, Object...params){
        return SpringBootJjjProperties.getCachePrefix() + "." + String.format(key, params);
    }
}
