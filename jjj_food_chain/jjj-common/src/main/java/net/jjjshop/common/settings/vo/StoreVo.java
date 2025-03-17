package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.OperateTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("系统设置VO")
public class StoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> deliveryType;

    //平台名称
    private String name;
    //平台logo
    private String logoUrl;
    // 商城LOGO
    //private String supplierImage;
    //会员默认昵称
    private String userName;
    //会员默认头像
    private String avatarUrl;
    //是否自动向微信小程序发货
    private Boolean isSendWx;
    // 登录logo
    private String loginLogo;
    // 登录描述
    private String loginDesc;
    //小程序是否开启手机号登录
    private Boolean wxPhone;
    private Boolean isGetLog;

    public StoreVo(){
        this.name = "玖玖珈商城";
        this.deliveryType = new ArrayList<>();
        this.isGetLog = false;
        this.avatarUrl = "https://food.jjjshop.net/image/user/avatarUrl.png";
        this.isSendWx = false;
        this.wxPhone = false;
        this.userName = "会员";
        this.loginLogo = "https://qn-cdn.jjjshop.net/202406141508488595d5235.png";
        this.loginDesc = "成为会员，立享更多优惠福利";
        this.logoUrl = "https://qn-cdn.jjjshop.net/20240627142611b653a3953.png";
    }
}
