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
 * 文件库分组记录表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_upload_group")
@ApiModel(value = "UploadGroup对象")
public class UploadGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类id")
    @TableId(value = "group_id", type = IdType.AUTO)
    private Integer groupId;

    @ApiModelProperty("文件类型image图片，video视频")
    private String groupType;

    @ApiModelProperty("分类名称")
    private String groupName;

    @ApiModelProperty("分类排序(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
