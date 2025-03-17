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
public class Banner implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Banner(String imagePath){
        this.item = new DiyItem();
        item.setName("图片轮播");
        item.setType("banner");
        item.setGroup("media");
        item.setIcon("icon-lunbotu");
        // 样式
        JSONObject style = new JSONObject();
        style.put("background", "#ffffff");
        style.put("bottomRadio", 0);
        style.put("btnColor", "#ffffff");
        style.put("btnShape", "round");
        style.put("height", 340);
        style.put("isLogin", 1);
        style.put("loginAfterRightTxtColor", "#FFFFFF");
        style.put("loginAfterTxtColor", "#FFFFFF");
        style.put("loginAvatarRadius", 100);
        style.put("loginAvatarSize", 80);
        style.put("loginBackground", "#FFCC00");
        style.put("loginBeforeTxtColor", "#FFFFFF");
        style.put("loginBgImage", imagePath + "image/diy/banner/11.jpg");
        style.put("loginBgType", 1);
        style.put("loginBottomRadio", 5);
        style.put("loginBtnBg", "#FFFFFF");
        style.put("loginBtnRadius", 15);
        style.put("loginBtnTxtColor", "#FFCC00");
        style.put("loginHeight", 42);
        style.put("loginMarginLeft", 10);
        style.put("loginOpacity", 100);
        style.put("loginPaddingLeft", 10);
        style.put("loginTopRadio", 5);
        style.put("paddingBottom", 0);
        style.put("paddingLeft", 0);
        style.put("paddingTop", 0);
        style.put("topRadio", 0);
        item.setStyle(style);

        // 默认数据
        JSONArray data = new JSONArray();
        JSONObject itemData1 = new JSONObject();
        itemData1.put("imgUrl", imagePath + "image/diy/banner/09.png");
        itemData1.put("linkUrl", "");
        JSONObject itemData2 = new JSONObject();
        itemData2.put("imgUrl", imagePath + "image/diy/banner/10.jpg");
        itemData2.put("linkUrl", "");
        // 加2条数据
        data.add(itemData1);
        data.add(itemData2);
        item.setData(data);
    }
}
