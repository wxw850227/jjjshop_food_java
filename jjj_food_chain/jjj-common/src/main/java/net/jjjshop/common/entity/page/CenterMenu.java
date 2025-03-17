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
 * 个人中心菜单
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_center_menu")
@ApiModel(value = "CenterMenu对象")
public class CenterMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("图片url")
    private String icon;

    @ApiModelProperty("排序(越小越靠前)")
    private Integer sort;

    @ApiModelProperty("跳转链接")
    private String path;

    @ApiModelProperty("链接名称")
    private String pathName;

    @ApiModelProperty("1显示0隐藏")
    private Boolean isShow;

    @ApiModelProperty("系统菜单标记")
    private String sysTag;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
