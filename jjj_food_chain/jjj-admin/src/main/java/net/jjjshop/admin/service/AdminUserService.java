

package net.jjjshop.admin.service;

import net.jjjshop.admin.vo.LoginAdminUserTokenVo;
import net.jjjshop.common.entity.admin.AdminUser;
import net.jjjshop.framework.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户 服务类
 */
public interface AdminUserService extends BaseService<AdminUser> {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    LoginAdminUserTokenVo login(String username, String password);

    /**
     * 修改密码
     * @param password
     * @return
     */
    Boolean renew(String password,String checkPass);
    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;
}
