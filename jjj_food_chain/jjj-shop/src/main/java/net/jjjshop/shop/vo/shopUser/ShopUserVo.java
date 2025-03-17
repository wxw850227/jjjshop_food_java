package net.jjjshop.shop.vo.shopUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.shop.vo.supplier.SupplierVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商城管理员VO")
public class ShopUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer shopUserId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("是否为超级管理员0不是,1是")
    private Boolean isSuper;

    @ApiModelProperty("是否禁用1禁用，0未禁用")
    private Integer isStatus;

    @ApiModelProperty("账号类型0总台1门店")
    private Integer userType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("角色名称")
    private List<Map> roleList;

    @ApiModelProperty("角色IDList")
    private List<Integer> roleArr;

    @ApiModelProperty("角色名称")
    private SupplierVo supplier;
}
