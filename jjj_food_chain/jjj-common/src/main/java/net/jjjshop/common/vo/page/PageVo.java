

package net.jjjshop.common.vo.page;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.page.Page;

@Data
@Accessors(chain = true)
@ApiModel("页面VO")
public class PageVo extends Page {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页面数据，json格式")
    private JSONObject pageDataJson;

    @ApiModelProperty("页面数据，json格式")
    private JSONArray items;

    @ApiModelProperty("页面数据，json格式")
    private JSONObject page;
}
