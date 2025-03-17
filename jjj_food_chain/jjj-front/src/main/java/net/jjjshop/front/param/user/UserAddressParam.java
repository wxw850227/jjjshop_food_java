package net.jjjshop.front.param.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@Accessors(chain = true)
@ApiModel(value = "CategoryParam对象", description = "Category新增修改参数")
public class UserAddressParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    @ApiModelProperty("电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phone;

    @ApiModelProperty("详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String detail;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ApiModelProperty("门牌号")
    @NotBlank(message = "门牌号不能为空")
    private String address;

    @ApiModelProperty("坐标经度")
    @NotBlank(message = "坐标经度不能为空")
    private String longitude;

    @ApiModelProperty("坐标纬度")
    @NotBlank(message = "坐标纬度不能为空")
    private String latitude;


}
