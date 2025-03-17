package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("交易设置VO")
public class AppShareVo implements Serializable {
    private static final long serialVersionUID = 1L;
    // 分享类型 1公众号/h5 2小程序 3下载页
    private Integer type;
    // 公众号、h5地址
    private String openSite;
    // 小程序原始id
    private String ghId;
    // 跳转网页
    private String webUrl;
    // 下载页
    private String downUrl;
    // 绑定类型
    private Integer bindType;

    public AppShareVo() {
        this.type = 1;
        this.openSite = "";
        this.ghId = "";
        this.webUrl = "";
        this.downUrl = "";
        this.bindType = 1;
    }
}
