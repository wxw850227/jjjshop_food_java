package net.jjjshop.shop.param.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 商品评价 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CommentParam对象", description = "商品评价参数")
public class CommentParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评价id")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @ApiModelProperty("评分 (10好评 20中评 30差评)")
    private Integer score;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("是否为图片评价")
    private Integer isPicture;

    @ApiModelProperty("评价排序")
    private Integer sort;

    @ApiModelProperty("评价状态(0=待审核 1=审核通过2=审核不通过)")
    private Integer status;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("订单商品id")
    private Integer orderProductId;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("软删除")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("商品图片主图")
    private String imagePath;

    @NotBlank(message = "产品名称不能为空")
    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品一口价")
    private BigDecimal productPrice;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("微信昵称")
    @TableField("nickName")
    private String nickName;

    @ApiModelProperty("评价图片")
    private List<String> image;
}
