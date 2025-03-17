package net.jjjshop.shop.service.page.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.CenterMenu;
import net.jjjshop.common.mapper.page.CenterMenuMapper;
import net.jjjshop.common.util.CentMenuUtils;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.page.CenterParam;
import net.jjjshop.shop.service.page.CenterMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 个人中心菜单 服务实现类
 * @author jjjshop
 * @since 2022-07-29
 */
@Slf4j
@Service
public class CenterMenuServiceImpl extends BaseServiceImpl<CenterMenuMapper, CenterMenu> implements CenterMenuService {

    /**
     * 列表
     * @param commonPageParam
     * @return
     */
    @Override
    public Paging<CenterMenu> getList(CommonPageParam commonPageParam){
        // 是否存在
        if(this.count(new LambdaQueryWrapper<>()) == 0){
            this.insertDefault();
        }
        Page<CenterMenu> page = new PageInfo<>(commonPageParam);
        IPage<CenterMenu> iPage = this.page(page, new LambdaQueryWrapper<CenterMenu>()
                .orderByAsc(CenterMenu::getSort));
        IPage<CenterMenu> result = iPage.convert(menu -> {
            if (!menu.getIcon().startsWith("http")) {
                menu.setIcon(SpringBootJjjProperties.getStaticAccessUrl() + menu.getIcon());
            }
            return menu;
        });
        return new Paging(result);
    }

    /**
     * 插入默认数据
     * @param
     * @return
     */
    private void insertDefault(){
        this.saveBatch(CentMenuUtils.getSysMenu());
    }

    /**
     * 新增
     * @param centerParam
     * @return
     */
    public Boolean add(CenterParam centerParam){
        CenterMenu menu = new CenterMenu();
        BeanUtils.copyProperties(centerParam, menu);
        return this.save(menu);
    }

    /**
     * 修改显示状态
     * @param menuId
     * @param isShow
     * @return
     */
    public Boolean status(Integer menuId, Boolean isShow){
        CenterMenu menu = new CenterMenu();
        menu.setMenuId(menuId);
        menu.setIsShow(isShow);
        menu.setUpdateTime(new Date());
        return this.updateById(menu);
    }

    /**
     * 修改
     * @param centerParam
     * @return
     */
    public Boolean edit(CenterParam centerParam){
        CenterMenu menu = new CenterMenu();
        BeanUtils.copyProperties(centerParam, menu);
        menu.setUpdateTime(new Date());
        return this.updateById(menu);
    }

    /**
     * 删除
     * @param menuId
     * @return
     */
    public Boolean delById(Integer menuId){
        return this.removeById(menuId);
    }
}
