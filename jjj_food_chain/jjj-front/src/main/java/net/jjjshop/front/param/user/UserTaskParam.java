package net.jjjshop.front.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "CategoryParam对象", description = "Category新增修改参数")
public class UserTaskParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务类型")
    private String taskType;

}
