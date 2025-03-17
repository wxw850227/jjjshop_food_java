package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@ApiModel("同城配送设置VO")
public class DeliverVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //默认配送方式
    private String defaults;
    private Map<String,Object> engine;

    @Data
    @Accessors(chain = true)
    @ApiModel("商家配送")
    public static class Local implements Serializable{
        private static final long serialVersionUID = 1L;
        //配送方式
        private String name;
        //自动配送时间(分钟)
        private Integer time;

        public Local(){
            this.name = "商家配送";
            this.time = 0;
        }
    }

    public DeliverVo() {
        this.defaults = "local";
        this.engine = new HashMap<>();
        engine.put("local",new Local());
    }

}
