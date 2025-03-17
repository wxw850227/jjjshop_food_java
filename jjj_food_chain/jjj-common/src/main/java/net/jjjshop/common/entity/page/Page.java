package net.jjjshop.common.entity.page;

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
 * diy页面表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_page")
@ApiModel(value = "Page对象")
public class Page implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页面id")
    @TableId(value = "page_id", type = IdType.AUTO)
    private Integer pageId;

    @ApiModelProperty("页面类型(10首页 20自定义页)")
    private Integer pageType;

    @ApiModelProperty("页面名称")
    private String pageName;

    @ApiModelProperty("页面数据")
    private String pageData;

    @ApiModelProperty("是否设置首页1是")
    private Boolean isDefault;

    @ApiModelProperty("appid")
    private Integer appId;

    @ApiModelProperty("软删除")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
