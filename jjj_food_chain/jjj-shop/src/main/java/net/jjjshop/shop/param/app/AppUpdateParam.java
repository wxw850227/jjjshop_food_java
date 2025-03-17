package net.jjjshop.shop.param.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "app升级参数", description = "app升级参数")
public class AppUpdateParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("update_id")
    private Integer updateId;

    @ApiModelProperty("appid")
    private Integer appId;

    @ApiModelProperty("android版本号")
    private String versionAndroid;

    @ApiModelProperty("ios版本号")
    private String versionIos;

    @ApiModelProperty("热更新包下载地址")
    private String wgtUrl;

    @ApiModelProperty("安卓整包升级地址")
    private String pkgUrlAndroid;

    @ApiModelProperty("ios整包升级地址")
    private String pkgUrlIos;
}
