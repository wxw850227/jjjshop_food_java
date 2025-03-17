package net.jjjshop.common.entity.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 商家用户权限表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_shop_access")
@ApiModel(value = "ShopAccess对象")
public class ShopAccess implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "主键id不能为空")
    @ApiModelProperty("主键id")
    @TableId(value = "access_id", type = IdType.INPUT)
    private Integer accessId;

    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("父级id")
    private Integer parentId;

    @ApiModelProperty("排序(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("重定向名称")
    private String redirectName;

    @ApiModelProperty("是否是路由 0=不是1=是")
    private Integer isRoute;

    @ApiModelProperty("是否是菜单 0不是 1是")
    private Integer isMenu;

    @ApiModelProperty("别名(废弃)")
    private String alias;

    @ApiModelProperty("是否显示1=显示0=不显示")
    private Integer isShow;

    @ApiModelProperty("插件分类id")
    private Integer plusCategoryId;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("是否门店菜单0否1是")
    private Integer isSupplier;

    @ApiModelProperty("app_id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
