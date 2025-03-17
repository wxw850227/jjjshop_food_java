package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("首页推送Vo")
public class HomePushVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isOpen;
    private String type;
    private String title;
    private String filePath;
    private Integer imageId;
    private String des;
    private String remark;
    private Link link;

    @Data
    @Accessors(chain = true)
    @ApiModel("首页推送链接Vo")
    public static class Link implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        private String name;
        private String type;
        private String url;

        public Link() {
            this.id = 0;
            this.name = "";
            this.type = "";
            this.url = "";
        }
    }

    public HomePushVo() {
        this.isOpen = false;
        this.type = "";
        this.title = "这是一个标题";
        this.filePath = "";
        this.imageId = 0;
        this.des = "";
        this.remark = "";
        this.link = new Link();
    }
}
