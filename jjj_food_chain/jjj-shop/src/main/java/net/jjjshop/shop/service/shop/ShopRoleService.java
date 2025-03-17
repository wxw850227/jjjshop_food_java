package net.jjjshop.shop.service.shop;

import net.jjjshop.common.entity.shop.ShopRole;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.shopUser.ShopRoleParam;

import java.util.List;

/**
 * 商家用户角色表 服务类
 * @author jjjshop
 * @since 2022-07-09
 */
public interface ShopRoleService extends BaseService<ShopRole> {
    /**
     * 角色列表
     * @return
     */
    List<ShopRole> getList();

    /**
     * 新增角色
     * @param shopRoleParam
     * @return
     */
    Boolean add(ShopRoleParam shopRoleParam);

    /**
     * 获取已选择权限
     * @param roleId
     * @return
     */
    List<Integer> getSelectList(Integer roleId);

    /**
     * 编辑角色
     * @param shopRoleParam
     * @return
     */
    Boolean edit(ShopRoleParam shopRoleParam);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    Boolean delete(Integer roleId);
}
