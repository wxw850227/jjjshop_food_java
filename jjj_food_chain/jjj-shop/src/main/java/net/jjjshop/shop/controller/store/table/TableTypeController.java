

package net.jjjshop.shop.controller.store.table;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.table.TableType;
import net.jjjshop.common.param.product.AttrParam;
import net.jjjshop.common.param.table.TableTypeParam;
import net.jjjshop.common.vo.product.ProductAttrVo;
import net.jjjshop.common.vo.store.table.TableTypeVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.shiro.vo.LoginShopAccessVo;
import net.jjjshop.framework.shiro.vo.LoginShopUserRedisVo;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.shopUser.ShopUserPageParam;
import net.jjjshop.shop.param.shopUser.ShopUserParam;
import net.jjjshop.shop.service.table.TableTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "index", tags = {"门店桌位类型"})
@RestController
@RequestMapping("/shop/store/table/type")
public class TableTypeController {
    @Autowired
    private TableTypeService tableTypeService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/type/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<TableType>> index() throws Exception{
        return ApiResult.ok(tableTypeService.getList());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/type/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated TableTypeParam param) {
        if(tableTypeService.add(param)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/type/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated TableTypeParam param) {
        if(tableTypeService.edit(param)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/store/table/type/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer typeId) {
        if(tableTypeService.delById(typeId)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }

}
