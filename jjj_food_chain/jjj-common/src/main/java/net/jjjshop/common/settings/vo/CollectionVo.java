package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("引导收藏VO")
public class CollectionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;

    public CollectionVo() {
        this.status = "0";
    }
}
