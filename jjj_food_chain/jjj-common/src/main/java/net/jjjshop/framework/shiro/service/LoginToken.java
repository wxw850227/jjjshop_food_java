

package net.jjjshop.framework.shiro.service;

import java.io.Serializable;

/**
 * 获取登录token
 **/
public interface LoginToken extends Serializable {

    /**
     * 获取登录token
     *
     * @return
     */
    String getToken();

}
