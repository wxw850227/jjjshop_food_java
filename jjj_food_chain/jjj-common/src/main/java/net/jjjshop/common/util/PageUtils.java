package net.jjjshop.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jjjshop.common.entity.page.Page;
import net.jjjshop.common.util.diy.DefaultItems;
import net.jjjshop.config.properties.SpringBootJjjProperties;

public class PageUtils {

    //生成默认page
    public static Page getPage(Integer pageType){
        Page page = new Page();
        page.setIsDefault(true);
        page.setPageType(pageType);
        JSONObject pageData = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject defaultPage = new JSONObject();
        //页面类型(10首页 20自定义页)
        if(pageType == 10){
            page.setPageName("首页");
            items = DefaultItems.getDefaultItemsArray(SpringBootJjjProperties.getStaticAccessUrl());
            defaultPage = getDefaultPage();
        }
        pageData.put("items",items);
        pageData.put("page",defaultPage.get("page"));
        page.setPageData(pageData.toJSONString());
        return page;
    }

    public static JSONObject getDefaultPage(){
        String imagePath = SpringBootJjjProperties.getStaticAccessUrl();
        JSONObject pageData = new JSONObject();
        JSONObject page = new JSONObject();
        page.put("type", "page");
        page.put("name", "页面设置");
        // 参数
        JSONObject params = new JSONObject();
        params.put("icon", "icon-biaoti");
        params.put("name", "页面名称");
        params.put("shareImg",   "https://qn-cdn.jjjshop.net/20241121093025399.png");
        params.put("shareTitle", "分享标题");
        params.put("title", "三勾点餐");
        page.put("params", params);
        //pageData
        pageData.put("page", page);
        pageData.put("items", new JSONArray());
        return pageData;
    }
}
