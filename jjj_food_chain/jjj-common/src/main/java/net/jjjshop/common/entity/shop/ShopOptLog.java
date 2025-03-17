package net.jjjshop.common.entity.shop;

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
 * 管理员操作记录表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_shop_opt_log")
@ApiModel(value = "ShopOptLog对象")
public class ShopOptLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "opt_log_id", type = IdType.AUTO)
    private Integer optLogId;

    @ApiModelProperty("用户id")
    private Integer shopUserId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("访问url")
    private String url;

    @ApiModelProperty("请求类型")
    private String requestType;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("浏览器信息")
    private String agent;

    @ApiModelProperty("操作内容")
    private String content;

    @ApiModelProperty("登录ip")
    private String ip;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
