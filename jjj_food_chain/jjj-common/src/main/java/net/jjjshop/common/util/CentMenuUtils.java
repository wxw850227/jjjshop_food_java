package net.jjjshop.common.util;

import net.jjjshop.common.entity.page.CenterMenu;

import java.util.ArrayList;
import java.util.List;

public class CentMenuUtils {
    /**
     * 默认系统菜单
     * @return
     */
    public static List<CenterMenu> getSysMenu(){
        List<CenterMenu> list = new ArrayList<>();
        list.add(getDefaultMenu("address", 1, "收货地址", "/pages/user/address/address", "链接到 页面 地址", "image/center_menu/address.png"));
        list.add(getDefaultMenu("setting",100, "设置", "/pages/user/set/set", "链接到 页面 设置", "image/center_menu/settings.png"));
        return list;
    }


    private static CenterMenu getDefaultMenu(String sysTag,Integer sort, String name, String path, String  pathName, String icon){
        CenterMenu menu = new CenterMenu();
        menu.setSysTag(sysTag);
        menu.setName(name);
        menu.setPath(path);
        menu.setPathName(pathName);
        menu.setIcon(icon);
        menu.setSort(sort);
        return menu;
    }
}
