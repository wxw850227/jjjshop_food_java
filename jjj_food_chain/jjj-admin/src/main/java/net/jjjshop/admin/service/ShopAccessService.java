package net.jjjshop.admin.service;

import net.jjjshop.admin.param.ShopAccessParam;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 商家用户权限表 服务类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
public interface ShopAccessService extends BaseService<ShopAccess> {
    /**
     * 获取未挂载到插件分类下的插件
     * @return
     */
    List<ShopAccess> getAllPlus();

    /**
     * 清除插件分类,不在插件分类下显示
     * @param accessId
     * @return
     */
    Boolean clearPlusCategoryId(Integer accessId);

    Boolean editPlusCategoryId(Integer accessId, Integer plusCategoryId);

    Boolean editStatusById(Integer accessId, Integer status);

    /**
     * 真删除
     * @param accessId
     * @return
     */
    Boolean delById(Integer accessId);
    /**
     * 新增
     * @param shopAccessParam
     * @return
     */
    Boolean add(ShopAccessParam shopAccessParam);
    /**
     * 修改
     * @param shopAccessParam
     * @return
     */
    Boolean edit(ShopAccessParam shopAccessParam);

    /**
     * 更改门店菜单状态
     */
    Boolean editSupplierStatusById(Integer id, Integer status);
}
