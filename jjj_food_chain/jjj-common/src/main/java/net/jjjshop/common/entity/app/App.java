package net.jjjshop.common.entity.app;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 微信小程序记录表
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjfood_app")
@ApiModel(value = "App对象")
public class App implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("应用id")
    @TableId(value = "app_id", type = IdType.AUTO)
    private Integer appId;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("logo")
    private Integer logo;

    @ApiModelProperty("通行证是否开发0,不开放1,开放")
    private Integer passportOpen;

    @ApiModelProperty("通行证类型10,微信开放平台,20手机号30,账号密码")
    private Integer passportType;

    @ApiModelProperty("是否回收")
    private Boolean isRecycle;

    @ApiModelProperty("是否连锁")
    private Boolean isChain;

    @ApiModelProperty("过期时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Date expireTime;

    @ApiModelProperty("微信服务商支付是否开启0否1是")
    private Boolean weixinService;

    @ApiModelProperty("支付类型，json格式")
    private String payType;

    @ApiModelProperty("微信支付方式v2：2,v3：3")
    private Integer wxPayKind;

    @ApiModelProperty("微信提现方式v2:2 v3:3")
    private Integer wxCashType;

    @ApiModelProperty("支付宝提现方式，1=手动打款， 2=审核通过自动转账")
    private Integer aliCashType;

    @ApiModelProperty("微信商户号id")
    private String mchid;

    @ApiModelProperty("微信支付密钥")
    private String apikey;

    @ApiModelProperty("p12证书")
    private byte[] p12;

    @ApiModelProperty("证书文件cert")
    private String certPem;

    @ApiModelProperty("证书文件key")
    private String keyPem;

    @ApiModelProperty("微信支付验签类型,0证书,1公钥")
    private Integer wxSignType;

    @ApiModelProperty("微信支付公钥文件Key")
    private String pubKeyPem ;

    @ApiModelProperty("微信平台证书序列号")
    private String wechatpaySerial;

    @ApiModelProperty("支付宝商户号")
    private String alipayAppid;

    @ApiModelProperty("支付宝公钥")
    private String alipayPublickey;

    @ApiModelProperty("应用私钥")
    private String alipayPrivatekey;

    @ApiModelProperty("是否删除")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
