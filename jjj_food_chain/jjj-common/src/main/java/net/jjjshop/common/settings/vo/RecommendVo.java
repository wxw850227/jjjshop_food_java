package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;


import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("商品推荐VO")
public class RecommendVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer isRecommend;
    private Integer[] location;
    private Integer choice;
    private String name;
    private Product[] product;
    private Integer num;
    private Integer type;

    @Data
    @Accessors(chain = true)
    @ApiModel("商品推荐VO")
    public static class Product implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer productId;
        private String productName;
        private String productImage;
        private Integer sort;

        public Product() {
            this.productId = 0;
            this.productName = "";
            this.productImage = "";
            this.sort = 1;
        }
    }

    public RecommendVo() {
        this.isRecommend = 1;
        this.location = new Integer[]{10,20,30};
        this.name = "";
        this.choice = 0;
        this.type = 10;
        this.num = 20;
    }
}
