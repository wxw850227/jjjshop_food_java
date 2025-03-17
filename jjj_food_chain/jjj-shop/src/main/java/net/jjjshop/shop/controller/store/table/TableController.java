

package net.jjjshop.shop.controller.store.table;

import cn.hutool.extra.qrcode.QrCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.table.TableArea;
import net.jjjshop.common.param.table.TableAreaParam;
import net.jjjshop.common.param.table.TableParam;
import net.jjjshop.common.util.poster.TablePosterUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.table.TablePageParam;
import net.jjjshop.shop.service.table.TableAreaService;
import net.jjjshop.shop.service.table.TableService;
import net.jjjshop.shop.service.table.TableTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"门店桌位"})
@RestController
@RequestMapping("/shop/store/table/table")
public class TableController {
    @Autowired
    private TableService tableService;
    @Autowired
    private TableAreaService tableAreaService;
    @Autowired
    private TableTypeService tableTypeService;
    @Autowired
    private TablePosterUtils tablePosterUtils;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/table/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(TablePageParam param) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("areaList",tableAreaService.getList());
        map.put("typeList",tableTypeService.getList());
        map.put("list",tableService.index(param));
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/table/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated TableParam param) {
        if(tableService.add(param)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/table/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated TableParam param) {
        if(tableService.edit(param)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/table/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer tableId) {
        if(tableService.delById(tableId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

    //桌位二维码
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    @RequiresPermissions("/store/table/table/qrcode")
    @OperationLog(name = "qrcode")
    @ApiOperation(value = "qrcode", response = String.class)
    public void qrcode(Integer id, String source, HttpServletResponse response) throws IOException {
        tablePosterUtils.genePoster(id,source,response);
    }

}
