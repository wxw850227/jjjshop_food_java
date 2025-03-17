package net.jjjshop.front.vo.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("文章VO")
public class UserSignListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer signDay;
}
