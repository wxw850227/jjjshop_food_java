package net.jjjshop.common.settings.vo.agent;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("分销商条件设置VO")
public class ConditionVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer become;
    private Integer becomeBuyProduct;
    private List<Integer> becomeBuyProductIds;
    private Integer downline;

    public ConditionVo() {
        this.become = 10;
        this.becomeBuyProduct = 0;
        this.becomeBuyProductIds = new ArrayList<>();
        this.downline = 10;
    }
}
