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
 * 轮播图组件
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("banner")
public class BannerMoRen implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public BannerMoRen(String imagePath){
        this.item = new DiyItem();
        item.setName("图片轮播");
        item.setType("banner");
        item.setGroup("media");
        item.setIcon("icon-lunbotu");
        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 0);
        style.put("paddingBottom", 0);
        style.put("paddingLeft", 0);
        style.put("topRadio", 0);
        style.put("bottomRadio", 0);
        style.put("btnColor", "#ffffff");
        style.put("background", "#ffffff");
        style.put("btnShape", "round");//rectangle 长方形，round圆形, square正方形
        style.put("height", 340);
        item.setStyle(style);

        // 默认数据
        JSONArray data = new JSONArray();
        JSONObject itemData1 = new JSONObject();
        itemData1.put("imgUrl", "https://qn-cdn.jjjshop.net/20241121091805588.png");
        itemData1.put("linkUrl", "");
        // 加2条数据
        data.add(itemData1);
        item.setData(data);
    }
}
