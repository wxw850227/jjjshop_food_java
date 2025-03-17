package net.jjjshop.common.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("前端图片VO")
public class ImageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //有很多模块都用到了该图片Vo，顾提取出来
    @ApiModelProperty("图片id")
    private Integer fileId;

    @ApiModelProperty("商品图片主图")
    private String filePath;
}
