package net.jjjshop.shop.param.setting;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "电子面单设置参数", description = "电子面单参数")
public class LabelSettingParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("电子面单设置id")
    @TableId(value = "label_setting_id", type = IdType.AUTO)
    private Integer labelSettingId;

    @ApiModelProperty("电子面单设置名称")
    @NotNull(message = "电子面单设置名称不能为空")
    private String settingName;

    @ApiModelProperty("物流公司id")
    @NotNull(message = "物流公司不能为空")
    private Integer expressId;

    @ApiModelProperty("电子面单客户账户或月结账号，需贵司向当地快递公司网点申请")
    @NotBlank(message = "电子面单月结账号不能为空")
    private String partnerId;

    @ApiModelProperty("电子面单密码")
    private String partnerKey;

    @ApiModelProperty("电子面单密钥")
    private String partnerSecret;

    @ApiModelProperty("电子面单客户账户名称")
    private String partnerName;

    @ApiModelProperty("收件网点名称")
    private String net;

    @ApiModelProperty("电子面单承载编号")
    private String code;

    @ApiModelProperty("电子面单承载快递员名")
    private String checkMan;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

}
