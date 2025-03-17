

package net.jjjshop.common.param.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "AttrParam对象", description = "Attr新增修改参数")
public class AttrParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer attributeId;

    @ApiModelProperty("属性名")
    @NotBlank(message = "属性名不能为空")
    private String attributeName;

    @ApiModelProperty("属性值List")
    private List<String> attributeValue;

    @ApiModelProperty("属性值List")
    private List<String> attributeValueList;

    @ApiModelProperty("排序")
    private Integer sort;
}
