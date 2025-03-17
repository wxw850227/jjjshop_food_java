package net.jjjshop.common.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("商品分类VO")
public class ProductImageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片id")
    private Integer imageId;

    @ApiModelProperty("商品图片主图")
    private String filePath;
}
