package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.config.properties.SpringBootJjjProperties;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("页面背景图")
public class BackgroundVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String url = SpringBootJjjProperties.getStaticAccessUrl();

    private String index;
    private String apply;
    private String cashApply;

    public BackgroundVo() {
        this.index = url + "image/agent/agent-bg.jpg";
        this.apply = url + "image/agent/agent-bg.jpg";
        this.cashApply = url + "image/agent/agent-bg.jpg";
    }
}
