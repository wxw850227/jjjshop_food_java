package net.jjjshop.common.util.diy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jjjshop.common.util.diy.items.*;

public class DefaultItems implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    public static JSONObject getDefaultItems(String imagePath){
        JSONObject items = new JSONObject();
        //图片轮播
        items.put("banner", new Banner(imagePath).getItem());
        //辅助空白
        items.put("blank", new Blank().getItem());
        //辅助线
        items.put("guide", new Guide().getItem());
        //导航组
        items.put("navBar", new NavBar(imagePath).getItem());
        //图片橱窗
        items.put("window", new Window(imagePath).getItem());
        //广告导航
        items.put("adNav", new AdNav(imagePath).getItem());
        return items;
    }

    public static JSONArray getDefaultItemsArray(String imagePath) {
        JSONArray items = new JSONArray();
        //图片轮播
        items.add(new BannerMoRen(imagePath).getItem());
        //广告导航
        items.add(new AdNavMoRen(imagePath).getItem());
        //导航组
        items.add( new NavBarMoRen(imagePath).getItem());
        return items;
    }
}
