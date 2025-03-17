package net.jjjshop.shop.service.page;

import net.jjjshop.common.entity.page.CenterMenu;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.page.CenterParam;

/**
 * 个人中心菜单 服务类
 * @author jjjshop
 * @since 2022-07-29
 */
public interface CenterMenuService extends BaseService<CenterMenu> {

    /**
     * 列表
     * @param commonPageParam
     * @return
     */
    Paging<CenterMenu> getList(CommonPageParam commonPageParam);

    /**
     * 新增
     * @param centerParam
     * @return
     */
    Boolean add(CenterParam centerParam);

    /**
     * 修改显示状态
     * @param menuId
     * @param isShow
     * @return
     */
    Boolean status(Integer menuId, Boolean isShow);

    /**
     * 修改
     * @param centerParam
     * @return
     */
    Boolean edit(CenterParam centerParam);

    /**
     * 删除
     * @param menuId
     * @return
     */
    Boolean delById(Integer menuId);
}
