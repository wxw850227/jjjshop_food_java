package net.jjjshop.shop.param.user;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "User对象")
public class UserParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @ApiModelProperty("微信openid(唯一标示)")
    private String openId;

    @ApiModelProperty("微信公众号openid")
    private String mpopenId;

    @ApiModelProperty("openappid")
    private String appopenId;

    @ApiModelProperty("微信开放平台id")
    private String unionId;

    @ApiModelProperty("苹果用户")
    private String appUser;

    @ApiModelProperty("注册来源")
    private String regSource;

    @ApiModelProperty("微信昵称")
    @TableField("nickName")
    private String nickName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("盐值")
    private String salt;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("微信头像")
    @TableField("avatarUrl")
    private String avatarUrl;

    @ApiModelProperty("性别0=女1=男2=未知")
    private Integer gender;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("默认收货地址")
    private Integer addressId;

    @ApiModelProperty("用户可用余额")
    private BigDecimal balance;

    @ApiModelProperty("用户可用积分")
    private Integer points;

    @ApiModelProperty("用户总支付的金额")
    private BigDecimal payMoney;

    @ApiModelProperty("实际消费的金额(不含退款)")
    private BigDecimal expendMoney;

    @ApiModelProperty("会员等级id")
    private Integer gradeId;

    @ApiModelProperty("推荐人id")
    private Integer refereeId;

    @ApiModelProperty("累计积分")
    private Integer totalPoints;

    @ApiModelProperty("总邀请人数")
    private Integer totalInvite;

    @ApiModelProperty("已冻结佣金")
    private BigDecimal freezeMoney;

    @ApiModelProperty("累积提现佣金")
    private BigDecimal cashMoney;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("用户余额充值方式")
    private String balanceMode;

    @ApiModelProperty("用户余额充值备注")
    private String balanceRemark;

    @ApiModelProperty("用户余额充值数额")
    private BigDecimal money;

    @ApiModelProperty("用户积分充值方式")
    private String pointsMode;

    @ApiModelProperty("用户积分充值备注")
    private String pointsRemark;

    @ApiModelProperty("用户积分充值数额")
    private BigDecimal value;

    @ApiModelProperty("用户充值方式,0表示余额充值，1表示积分充值")
    private Integer source;

    @ApiModelProperty("等级变更备注")
    private String remark;

    @ApiModelProperty("用户标签")
    private List<Integer> checkedTag;
}
