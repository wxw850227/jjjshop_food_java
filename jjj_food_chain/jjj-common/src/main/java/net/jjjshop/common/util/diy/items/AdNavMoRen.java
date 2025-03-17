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
public class AdNavMoRen implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public AdNavMoRen(String imagePath){
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
        style.put("topRadio", 8);
        style.put("paddingTop", 10);

        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        item.setParams(params);

        // 加2条数据
        JSONArray data = new JSONArray();
        JSONObject itemData1 = new JSONObject();
        itemData1.put("imgUrl",  "https://qn-cdn.jjjshop.net/20241121092021721.png");
        itemData1.put("linkUrl", "pages/product/list/takeaway?orderType=takein");
        itemData1.put("text", "下单免排队");
        itemData1.put("textcolor", "#999");
        itemData1.put("title", "门店自取");
        itemData1.put("titlecolor", "#333");
        itemData1.put("name", "链接到 页面 自取");
        JSONObject itemData2 = new JSONObject();
        itemData2.put("imgUrl",  "https://qn-cdn.jjjshop.net/20241121092116838.png");
        itemData2.put("linkUrl", "pages/product/list/takeaway?orderType=takeout");
        itemData2.put("text", "在家享美味");
        itemData2.put("textcolor", "#999");
        itemData2.put("title", "外卖点单");
        itemData2.put("titlecolor", "#333");
        itemData2.put("name", "链接到 页面 外卖");
        JSONObject itemData3 = new JSONObject();
        itemData3.put("imgUrl",  "https://qn-cdn.jjjshop.net/20241121092141175.png");
        itemData3.put("linkUrl", "scanQrcode");
        itemData3.put("text", "下单更便捷");
        itemData3.put("textcolor", "#999");
        itemData3.put("title", "扫码点餐");
        itemData3.put("titlecolor", "#333");
        itemData3.put("name", "链接到 菜单 扫一扫");
        data.add(itemData1);
        data.add(itemData2);
        data.add(itemData3);
        item.setData(data);
    }
}
