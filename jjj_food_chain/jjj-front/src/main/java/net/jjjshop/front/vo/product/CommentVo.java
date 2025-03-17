package net.jjjshop.front.vo.product;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品评价VO")
public class CommentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评价id")
    private Integer commentId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("商品规格")
    private String productAttr;

    @ApiModelProperty("商品图片")
    private String productImage;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户微信头像")
    private String avatarUrl;

    @ApiModelProperty("评分 (10好评 20中评 30差评)")
    private Integer score;

    @ApiModelProperty("微信昵称")
    private String nickName;

    @ApiModelProperty("评价图片")
    private List<String> image;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("评价用户")
    private JSONObject users;

}
