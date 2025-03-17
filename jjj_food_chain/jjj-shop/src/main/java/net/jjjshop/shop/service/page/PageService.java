package net.jjjshop.shop.service.page;

import net.jjjshop.common.entity.page.Page;
import net.jjjshop.common.vo.page.PageVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;

import java.util.List;

/**
 * diy页面表 服务类
 * @author jjjshop
 * @since 2022-07-28
 */
public interface PageService extends BaseService<Page> {

    /**
     * 列表
     * @param commonPageParam
     * @param pageType
     * @return
     */
    Paging<Page> getList(CommonPageParam commonPageParam, Integer pageType);

    /**
     * 默认首页
     * @param
     * @return
     */
    PageVo getDefault(Integer pageType);

    /**
     * 详情
     * @param pageId
     * @return
     */
    PageVo detail(Integer pageId);

    /**
     * 修改
     * @param params
     * @return
     */
    Boolean edit(String params);

    /**
     * 修改默认首页
     * @param pageId
     * @return
     */
    Boolean setDefaultPage(Integer pageId,Integer pageType);

    /**
     * 新增
     * @param pageType
     * @param params
     * @return
     */
    Boolean add(Integer pageType, String params);

    /**
     * 删除
     * @param pageId
     * @return
     */
    Boolean setDelete(Integer pageId);

    /**
     * 所有
     * @param pageType
     * @return
     */
    List<Page> getAll(Integer pageType);
}
