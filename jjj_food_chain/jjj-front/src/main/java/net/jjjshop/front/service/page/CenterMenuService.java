package net.jjjshop.front.service.page;

import net.jjjshop.common.entity.page.CenterMenu;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 个人中心菜单 服务类
 * @author jjjshop
 * @since 2022-07-29
 */
public interface CenterMenuService extends BaseService<CenterMenu> {
    /**
     * 获取菜单
     * @return
     */
    List<CenterMenu> getMenus();
}
