package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.config.properties.SpringBootJjjProperties;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("分销海报")
public class QrcodeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String url = SpringBootJjjProperties.getStaticAccessUrl();

    private Backdrop backdrop;
    private NickName nickName;
    private Avatar avatar;
    private Qrcode qrcode;

    @Data
    @Accessors(chain = true)
    @ApiModel("海报背景图")
    public static class Backdrop implements Serializable {
        private static final long serialVersionUID = 1L;
        private String src;

        public Backdrop() {
            this.src = url + "image/agent/backdrop.jpg";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("微信昵称")
    public static class NickName implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer fontSize;
        private String color;
        private Integer left;
        private Integer top;

        public NickName() {
            this.fontSize = 14;
            this.color = "#000000";
            this.left = 150;
            this.top = 99;
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("微信头像")
    public static class Avatar implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer width;
        private String style;
        private Integer left;
        private Integer top;

        public Avatar() {
            this.width = 70;
            this.style = "circle";
            this.left = 150;
            this.top = 18;
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("推广二维码")
    public static class Qrcode implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer width;
        private String style;
        private Integer left;
        private Integer top;

        public Qrcode() {
            this.width = 100;
            this.style = "circle";
            this.left = 136;
            this.top = 128;
        }
    }

    public QrcodeVo() {
        this.backdrop = new Backdrop();
        this.nickName = new NickName();
        this.avatar = new Avatar();
        this.qrcode = new Qrcode();
    }

}
