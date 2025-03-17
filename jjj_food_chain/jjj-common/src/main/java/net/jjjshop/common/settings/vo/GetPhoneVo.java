package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("获取手机号VO")
public class GetPhoneVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //页面类型
    private List<Integer> checkedCities;
    //拒绝后几天不再提醒，设置为0则每次都要提醒
    private Integer sendDay;

    public GetPhoneVo() {
        this.checkedCities = new ArrayList<>();
        this.sendDay = 7;
    }
}
