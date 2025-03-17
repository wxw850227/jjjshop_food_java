package net.jjjshop.shop.service.page.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.PageCategory;
import net.jjjshop.common.mapper.page.PageCategoryMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.page.PageCategoryService;
import org.springframework.stereotype.Service;

/**
 * app分类页模板 服务实现类
 * @author jjjshop
 * @since 2022-07-28
 */
@Slf4j
@Service
public class PageCategoryServiceImpl extends BaseServiceImpl<PageCategoryMapper, PageCategory> implements PageCategoryService {
}
