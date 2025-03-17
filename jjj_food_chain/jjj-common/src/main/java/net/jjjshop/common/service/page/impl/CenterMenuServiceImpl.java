package net.jjjshop.common.service.page.impl;

import net.jjjshop.common.entity.page.CenterMenu;
import net.jjjshop.common.mapper.page.CenterMenuMapper;
import net.jjjshop.common.service.page.CenterMenuService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 个人中心菜单 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-29
 */
@Slf4j
@Service
public class CenterMenuServiceImpl extends BaseServiceImpl<CenterMenuMapper, CenterMenu> implements CenterMenuService {

    @Autowired
    private CenterMenuMapper centerMenuMapper;

}
