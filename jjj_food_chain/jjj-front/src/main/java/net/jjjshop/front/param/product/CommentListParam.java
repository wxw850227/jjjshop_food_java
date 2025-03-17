package net.jjjshop.front.param.product;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.vo.setting.ImageVo;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 商品评价 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CommentListParam对象", description = "评价列表参数对象")
public class CommentListParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论图片id")
    private Integer commentId;

    @ApiModelProperty("评分 (10好评 20中评 30差评)")
    private Integer score;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("订单商品id")
    private Integer orderProductId;

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

    @ApiModelProperty("图片")
    private List<ImageVo> imageList;
}
