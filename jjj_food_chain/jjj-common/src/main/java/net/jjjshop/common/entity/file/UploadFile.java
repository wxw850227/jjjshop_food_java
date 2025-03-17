package net.jjjshop.common.entity.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件库记录表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_upload_file")
@ApiModel(value = "UploadFile对象")
public class UploadFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件id")
    @TableId(value = "file_id", type = IdType.AUTO)
    private Integer fileId;

    @ApiModelProperty("存储方式")
    private String storage;

    @ApiModelProperty("文件分组id")
    private Integer groupId;

    @ApiModelProperty("存储域名")
    private String fileUrl;

    @ApiModelProperty("保存路径")
    private String saveName;

    @ApiModelProperty("文件路径")
    private String fileName;

    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件真实名")
    private String realName;

    @ApiModelProperty("文件扩展名")
    private String extension;

    @ApiModelProperty("是否为c端用户上传")
    private Integer isUser;

    @ApiModelProperty("是否已回收")
    private Integer isRecycle;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("软删除")
    private Integer isDelete;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
