

package net.jjjshop.config.properties;

import lombok.Data;

/**
 * Shiro权限配置映射类
 **/
@Data
public class ShiroPermissionProperties {

    /**
     * 路径
     */
    private String url;
    /**
     * 路径数组
     */
    private String[] urls;

    /**
     * 权限
     */
    private String permission;

}
