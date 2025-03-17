

package net.jjjshop.shop.service.shop;

import net.jjjshop.common.entity.shop.ShopUser;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.shopUser.ShopUserPageParam;
import net.jjjshop.shop.param.shopUser.ShopUserParam;
import net.jjjshop.framework.shiro.vo.LoginShopUserTokenVo;
import net.jjjshop.shop.vo.shopUser.ShopUserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户 服务类
 */
public interface ShopUserService extends BaseService<ShopUser> {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    LoginShopUserTokenVo login(String username, String password);

    /**
     * 修改密码
     * @param password
     * @return
     */
    Boolean renew(String oldpass, String password, String confirmPass);
    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;

    /**
     * 列表
     * @param params
     * @throws Exception
     */
    Paging<ShopUserVo> getList(ShopUserPageParam params);

    /**
     * 新增用户
     * @param shopUserParam
     * @return
     */
    Boolean add(ShopUserParam shopUserParam);

    /**
     * 编辑用户
     * @param shopUserParam
     * @return
     */
    Boolean edit(ShopUserParam shopUserParam);

    /**
     * 删除用户
     * @param shopUserId
     * @return
     */
    Boolean setDelete(Integer shopUserId);

    boolean setStatus(ShopUserParam shopUserParam);

    ShopUserVo detail(Integer shopUserId);
}
