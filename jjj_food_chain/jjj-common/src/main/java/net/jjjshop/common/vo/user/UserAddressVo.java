package net.jjjshop.common.vo.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.UserAddress;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("用户地址VO")
public class UserAddressVo extends UserAddress {

    @ApiModelProperty("0超出配送距离,1正常")
    private Integer status;

    @ApiModelProperty("距离")
    private BigDecimal distance;

}
