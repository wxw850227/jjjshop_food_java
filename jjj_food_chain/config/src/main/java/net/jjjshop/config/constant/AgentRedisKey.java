

package net.jjjshop.config.constant;

/**
 *  分销商 redis key 常量
 **/
public interface AgentRedisKey {

    // --------------------admin端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String ADMIN_LOGIN_TOKEN = "admin:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String ADMIN_LOGIN_USER = "admin:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String ADMIN_LOGIN_SALT = "admin:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String ADMIN_LOGIN_USER_TOKEN = "admin:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String ADMIN_LOGIN_USER_ALL_TOKEN = "admin:login:user:token:%s:*";

    // --------------------admin端登录信息结束----------------------

    // --------------------shop端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String SHOP_LOGIN_TOKEN = "shop:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String SHOP_LOGIN_USER = "shop:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String SHOP_LOGIN_SALT = "shop:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String SHOP_LOGIN_USER_TOKEN = "shop:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String SHOP_LOGIN_USER_ALL_TOKEN = "shop:login:user:token:%s:*";

    // --------------------shop端登录信息结束----------------------

    // --------------------front端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String USER_LOGIN_TOKEN = "user:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String USER_LOGIN_USER = "user:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String USER_LOGIN_SALT = "user:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String USER_LOGIN_USER_TOKEN = "user:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String USER_LOGIN_USER_ALL_TOKEN = "user:login:user:token:%s:*";

    // --------------------front端登录信息结束----------------------


    /**
     * 验证码
     * verify.code:666666
     */
    String VERIFY_CODE = "verify.code:%s";

    /**
     * 地区缓存
     */
    String REGION_DATA = "region.data";
    /**
     * 设置缓存
     */
    String SETTING_DATA = "agent.setting.data:%s:%s";
    /**
     * 所有设置缓存
     */
    String SETTING_DATA_ALL = "setting.data:%s:*";
    /**
     * 商品分类缓存
     */
    String PRODUCT_CATEGORY_DATA = "product.category.data:%s";
}
