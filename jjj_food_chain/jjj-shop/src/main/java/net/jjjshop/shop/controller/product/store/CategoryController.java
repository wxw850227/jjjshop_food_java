package net.jjjshop.shop.controller.product.store;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.product.CategoryParam;
import net.jjjshop.shop.service.product.ProductCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "index", tags = {"店内商品分类管理"})
@RestController
@RequestMapping("/shop/product/store/category")
public class CategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    //普通分类列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/product/takeaway/category/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<CategoryVo>> index(@RequestBody CategoryParam param) throws Exception{
        //0外卖1店内
        param.setType(1);
        //0普通1特殊
        param.setIsSpecial(0);
        return ApiResult.ok(productCategoryService.getIndex(param));
    }

    //特殊分类列表
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @RequiresPermissions("/product/takeaway/category/list")
    @OperationLog(name = "list")
    @ApiOperation(value = "list", response = String.class)
    public ApiResult<List<CategoryVo>> list(@RequestBody CategoryParam param) throws Exception{
        //0外卖1店内
        param.setType(1);
        //0普通1特殊
        param.setIsSpecial(1);
        return ApiResult.ok(productCategoryService.getIndex(param));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/product/takeaway/category/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated CategoryParam categoryParam) {
        //0外卖1店内
        categoryParam.setType(1);
        //0普通1特殊
        categoryParam.setIsSpecial(0);
        if(productCategoryService.add(categoryParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/product/takeaway/category/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated CategoryParam categoryParam) {
        if(productCategoryService.edit(categoryParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    //修改显示状态
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    @RequiresPermissions("/product/takeaway/category/set")
    @OperationLog(name = "set")
    @ApiOperation(value = "set", response = String.class)
    public ApiResult<String> set(Integer categoryId) {
        if(productCategoryService.set(categoryId)) {
            return ApiResult.ok(null, "设置成功");
        }else{
            return ApiResult.fail("设置失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/product/takeaway/category/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(@RequestBody  CategoryParam categoryParam) {
        if(productCategoryService.delById(categoryParam.getCategoryId())) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }
}
