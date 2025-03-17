package net.jjjshop.shop.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.file.UploadFile;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("上传文件VO")
public class UploadFileVo extends UploadFile {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件路径")
    private String filePath;
}
