package net.jjjshop.front.service.page.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.Page;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.page.PageMapper;
import net.jjjshop.common.util.PageUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.service.page.PageService;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.common.vo.product.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * diy页面表 服务实现类
 * @author jjjshop
 * @since 2022-07-28
 */
@Slf4j
@Service
public class PageServiceImpl extends BaseServiceImpl<PageMapper, Page> implements PageService {

    @Autowired
    private ProductService productService;
    @Autowired
    private SettingUtils settingUtils;

    /**
     * 默认首页
     * @param user
     * @param pageId
     * @return
     */
    public synchronized JSONObject getPageData(User user, Integer pageId,Integer pageType){
        // 如果pageId为空，则查首页
        Page page = null;
        LambdaQueryWrapper<Page> wrapper = new LambdaQueryWrapper<Page>()
                //页面类型(10首页 20自定义页 30个人中心)
                .eq(Page::getPageType, pageType)
                .eq(Page::getIsDelete, 0)
                .eq(Page::getIsDefault, true)
                .orderByDesc(Page::getPageId)
                .last(" limit 1");
        if(pageId == null || pageId <= 0){
            page = this.getOne(wrapper);
            if(page == null){
                //生成默认页
                page = PageUtils.getPage(pageType);
                int num  = this.count(new LambdaQueryWrapper<Page>()
                        .eq(Page::getPageType, pageType)
                        .eq(Page::getIsDelete, 0)
                        .eq(Page::getIsDefault, true));
                //防止重复
                if(num == 0){
                    this.save(page);
                }
            }
        }else{
            page = this.getOne(new LambdaQueryWrapper<Page>().eq(Page::getPageId, pageId));
        }
        JSONObject pageData = JSON.parseObject(page.getPageData());
        JSONArray items = pageData.getJSONArray("items");
        int len = items.size() - 1;
        return pageData;
    }

}
