package net.jjjshop.front.controller.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.order.CategoryListParam;
import net.jjjshop.front.param.product.CategoryParam;
import net.jjjshop.front.service.product.ProductCategoryService;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.vo.supplier.SupplierVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "productCategory", tags = {"商品分类管理"})
@RestController
@RequestMapping("/front/product/category")
public class CategoryController extends BaseController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(@RequestBody CategoryListParam param) {
        User user = this.getUser(false);
        Map<String, Object> result = new HashMap<>();
        SupplierVo supplierVo = supplierService.getDetail(param);
        param.setShopSupplierId(supplierVo.getShopSupplierId());
        result.put("list", productCategoryService.getList(param));
        result.put("addressId", user == null?0 : user.getAddressId());
        result.put("supplier", supplierVo);
        return ApiResult.ok(result);
    }
}
