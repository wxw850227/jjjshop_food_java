

package net.jjjshop.common.param.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "FeedParamParam对象", description = "FeedParam新增修改参数")
public class FeedParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Integer feedId;

    @ApiModelProperty("属性名")
    @NotBlank(message = "属性名不能为空")
    private String feedName;

    @ApiModelProperty("价格")
    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
