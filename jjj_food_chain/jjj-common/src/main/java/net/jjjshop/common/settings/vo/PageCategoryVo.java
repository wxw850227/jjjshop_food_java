package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("主题设置VO")
public class PageCategoryVo implements Serializable {
    private static final long serialVersionUID = 1L;
    // 分类，10=一级分类，20=二级分类
    private Integer categoryStyle;
    // 分享标题
    private String shareTitle;
    //样式
    private Integer windStyle;

    public PageCategoryVo() {
        this.categoryStyle = 10;
        this.shareTitle = "";
        this.windStyle = 1;
    }

}
