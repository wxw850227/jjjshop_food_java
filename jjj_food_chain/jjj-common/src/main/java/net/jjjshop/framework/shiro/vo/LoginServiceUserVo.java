

package net.jjjshop.framework.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 登录用户对象，响应给前端
 **/
@Data
@Accessors(chain = true)
public class LoginServiceUserVo implements Serializable {

    private static final long serialVersionUID = -1758338570596088158L;

    @ApiModelProperty("主键")
    private Integer serviceUserId;

    @ApiModelProperty("绑定的用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String password;

    @ApiModelProperty("客服名称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headPortrait;

    @ApiModelProperty("关联供应商id")
    private Integer shopSupplierId;

    @ApiModelProperty("客服类型，1=商户，2=平台")
    private Integer type;

    @ApiModelProperty("账号状态，0=关闭，1=正常")
    private Integer statu;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
