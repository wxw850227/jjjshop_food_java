

package net.jjjshop.shop.controller.page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.util.PageUtils;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.common.util.diy.DefaultItems;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.service.page.PageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"首页装修"})
@RestController
@RequestMapping("/shop/page/page")
public class PageController {
    @Autowired
    private PageService pageService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @RequiresPermissions("/page/page/home")
    @OperationLog(name = "list")
    @ApiOperation(value = "list", response = String.class)
    public ApiResult<Map<String,Object>> list(@Validated @RequestBody CommonPageParam commonPageParam) throws Exception{
        Map<String,Object> result = new HashMap<>();
        result.put("list", pageService.getList(commonPageParam, 10));
        result.put("default", pageService.getDefault(10));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @RequiresPermissions("/page/page/home")
    @OperationLog(name = "home")
    @ApiOperation(value = "home", response = String.class)
    public ApiResult<Map<String,Object>> toEditPage() {
        Map<String,Object> result = new HashMap<>();
        result.put("jsonData", pageService.getDefault(10).getPageDataJson());
        result.put("defaultData", DefaultItems.getDefaultItems(SpringBootJjjProperties.getStaticAccessUrl()));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/page/page/home")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> editPage(String params) {
        if(pageService.edit(params)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/page/page/home")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody CommonPageParam commonPageParam) throws Exception{
        Map<String,Object> result = new HashMap<>();
        result.put("list", pageService.getList(commonPageParam, 20));;
        return ApiResult.ok(result);
    }
}
