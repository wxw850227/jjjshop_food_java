package net.jjjshop.front.vo.order;


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

@Data
@Accessors(chain = true)
@ApiModel("订单评价VO")
public class OrderCommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("微信昵称")
    @TableField("nickName")
    private String nickName;

    @ApiModelProperty("图片")
    private String productImage;

    @ApiModelProperty("图片")
    private List<ImageVo> imageList;
}
