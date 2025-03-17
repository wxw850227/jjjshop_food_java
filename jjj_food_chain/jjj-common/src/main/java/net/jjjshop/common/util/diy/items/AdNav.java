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
@ApiModel("adNav")
public class AdNav implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public AdNav(String imagePath){
        this.item = new DiyItem();
        item.setName("广告导航");
        item.setType("adNav");
        item.setGroup("shop");
        item.setIcon("icon-tupian11");
        // 样式
        JSONObject style = new JSONObject();
        style.put("bgcolor", "#f2f2f2");
        style.put("bottomRadio", 8);
        style.put("paddingLeft", 10);
        style.put("paddingTop", 10);
        style.put("topRadio", 8);

        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        item.setParams(params);

        // 加2条数据
        JSONArray data = new JSONArray();
        JSONObject itemData1 = new JSONObject();
        itemData1.put("imgUrl", imagePath + "image/diy/banner/03.png");
        itemData1.put("linkUrl", "pages/product/list/takeaway?orderType=takein");
        itemData1.put("text", "下单免排队");
        itemData1.put("textcolor", "#999");
        itemData1.put("title", "门店自取");
        itemData1.put("titlecolor", "#333");
        JSONObject itemData2 = new JSONObject();
        itemData2.put("imgUrl", imagePath + "image/diy/banner/04.png");
        itemData2.put("linkUrl", "pages/product/list/takeaway?orderType=takeout");
        itemData2.put("text", "无接触配送");
        itemData2.put("textcolor", "#999");
        itemData2.put("title", "外卖点单");
        itemData2.put("titlecolor", "#333");
        data.add(itemData1);
        data.add(itemData2);
        item.setData(data);
    }
}
