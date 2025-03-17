package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("系统设置VO")
public class SysConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String shopName;
    private String shopBgImg;
    private String shopLogoImg;
    private String cashierName;
    private String cashierBgImg;
    private Boolean serviceOpen;
    private WeixinService weixinService;

    @Data
    @Accessors(chain = true)
    @ApiModel("系统设置VO")
    public static class WeixinService implements Serializable{
        private static final long serialVersionUID = 1L;
        private Boolean isOpen;
        private String appId;
        private String mchId;
        private String apiKey;
        private String certPem;
        private String keyPem;
        public WeixinService(){
            this.isOpen = false;
            this.appId = "";
            this.mchId = "";
            this.apiKey = "";
            this.certPem = "";
            this.keyPem = "";
        }
    }

    public SysConfigVo(){
        this.shopName = "点餐管理系统";
        this.shopBgImg = "";
        this.shopLogoImg = "";
        this.cashierName = "收银台";
        this.cashierBgImg = "";
        this.serviceOpen = false;
        this.weixinService = new WeixinService();
    }
}
