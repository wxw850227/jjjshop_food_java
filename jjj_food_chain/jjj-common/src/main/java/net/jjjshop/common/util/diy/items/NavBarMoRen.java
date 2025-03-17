package net.jjjshop.common.util.diy.items;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.util.diy.DiyItem;

/**
 * 导航组组件
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("navBar")
public class NavBarMoRen implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public NavBarMoRen(String imagePath){
        this.item = new DiyItem();
        item.setName("导航组");
        item.setType("navBar");
        item.setGroup("media");
        item.setIcon("icon-mulu");
        // 样式
        JSONObject style = new JSONObject();
        style.put("background", "#ffffff");
        style.put("rowsNum", 1);
        style.put("bgcolor", "#f2f2f2");
        style.put("paddingTop", 10);
        style.put("paddingBottom", 10);
        style.put("paddingLeft", 10);
        style.put("topRadio", 8);
        style.put("bottomRadio", 8);
        style.put("background1", "#FFFFFF");
        style.put("background2", "#FFFFFF");
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        item.setParams(params);

        // 加条数据
        JSONArray data = new JSONArray();
        JSONObject itemData1 = new JSONObject();
        itemData1.put("color", "#666666");
        itemData1.put("imgUrl",  "https://qn-cdn.jjjshop.net/20241121092204174.png");
        itemData1.put("linkUrl", "pages/product/list/store");
        itemData1.put("name", "链接到 页面 快餐模式");
        itemData1.put("text", "带你发掘更多美食");
        itemData1.put("title", "堂食点餐");

        data.add(itemData1);
        item.setData(data);
    }
}
