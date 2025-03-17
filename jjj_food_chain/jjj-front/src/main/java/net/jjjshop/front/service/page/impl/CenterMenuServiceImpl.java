package net.jjjshop.front.service.page.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.CenterMenu;
import net.jjjshop.common.mapper.page.CenterMenuMapper;
import net.jjjshop.common.util.CentMenuUtils;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.page.CenterMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心菜单 服务实现类
 * @author jjjshop
 * @since 2022-07-29
 */
@Slf4j
@Service
public class CenterMenuServiceImpl extends BaseServiceImpl<CenterMenuMapper, CenterMenu> implements CenterMenuService {

    @Autowired
    private CenterMenuService centerMenuService;

    /**
     * 获取菜单
     * @param
     * @return
     */
    @Override
    public List<CenterMenu> getMenus() {
        List<CenterMenu> menus = CentMenuUtils.getSysMenu();
        // 查询用户菜单
        List<CenterMenu> userMenus = this.getAll();
        List<String> userMenuTags = new ArrayList<>();
        for (CenterMenu menu : userMenus) {
            if (StringUtils.isNotBlank(menu.getSysTag())) {
                userMenuTags.add(menu.getSysTag());
            }
        }
        List<CenterMenu> saveData = new ArrayList<>();
        for (CenterMenu menu : menus) {
            if (StringUtils.isNotBlank(menu.getSysTag()) && !userMenuTags.contains(menu.getSysTag())) {
                menu.setSort(menu.getSort());
                saveData.add(menu);
            }
        }
        if (saveData.size() > 0) {
            this.saveBatch(saveData);
            // 重新查询
            userMenus = this.getAll();
        }
        List<CenterMenu> showMenus = new ArrayList<>();
        for (CenterMenu menu : userMenus) {
            if (!menu.getIcon().startsWith("http")) {
                menu.setIcon(SpringBootJjjProperties.getStaticAccessUrl() + menu.getIcon());
            }
            if (menu.getIsShow()) {
                showMenus.add(menu);
            }
        }
        return showMenus;
    }

    /**
     * 所有菜单
     * @return
     */
    private List<CenterMenu> getAll() {
        List<CenterMenu> list = this.list(new LambdaQueryWrapper<CenterMenu>().orderByAsc(CenterMenu::getSort));
        if (list.size() == 0) {
            this.saveBatch(CentMenuUtils.getSysMenu());
            list = this.list(new LambdaQueryWrapper<CenterMenu>().orderByAsc(CenterMenu::getSort));
        }
        return list;
    }
}
