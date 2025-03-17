package net.jjjshop.common.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("自提门店保存信息Vo")
public class ExtractSaveVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("联系人")
    private String linkman;

    @ApiModelProperty("联系人电话")
    private String phone;

    @ApiModelProperty("自提门店Id")
    private Integer storeId;
}
