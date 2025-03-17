package net.jjjshop.shop.vo.app;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.app.App;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("appVo")
public class AppVo extends App {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付类型")
    private JSONObject payTypeJson;
}
