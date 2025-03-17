package net.jjjshop.common.service.page.impl;

import net.jjjshop.common.entity.page.PageCategory;
import net.jjjshop.common.mapper.page.PageCategoryMapper;
import net.jjjshop.common.service.page.PageCategoryService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * app分类页模板 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-28
 */
@Slf4j
@Service
public class PageCategoryServiceImpl extends BaseServiceImpl<PageCategoryMapper, PageCategory> implements PageCategoryService {

    @Autowired
    private PageCategoryMapper pageCategoryMapper;

}
