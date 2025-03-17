package net.jjjshop.front.service.page;

import com.alibaba.fastjson.JSONObject;
import net.jjjshop.common.entity.page.Page;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;

/**
 * diy页面表 服务类
 * @author jjjshop
 * @since 2022-07-28
 */
public interface PageService extends BaseService<Page> {
    /**
     * 页面数据
     * @param user
     * @param pageId
     * @return
     */
    JSONObject getPageData(User user, Integer pageId,Integer pageType);
}
