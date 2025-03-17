package net.jjjshop.front.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserAddress;

@Data
@Accessors(chain = true)
@ApiModel("用户VO")
public class UserVo extends User {


    @ApiModelProperty("优惠券数量")
    private Integer coupon;

    @ApiModelProperty("默认地址")
    private UserAddress addressDefault;

}
