package net.jjjshop.shop.controller.data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.product.ProductPageParam;
import net.jjjshop.shop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "查询商品数据", tags = {"查询商品数据"})
@RestController
@RequestMapping("/shop/data/product")
public class ProductDataController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductUtils productUtils;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody ProductPageParam productPageParam) throws Exception{
        productPageParam.setType("sell");
        return ApiResult.ok(productService.getList(productPageParam));
    }

    @RequestMapping(value = "/spec", method = RequestMethod.POST)
    @OperationLog(name = "spec")
    @ApiOperation(value = "spec", response = String.class)
    public ApiResult<Map<String, Object>> spec(Integer productId) throws Exception{
        Product product = productService.getById(productId);
        List<ProductSkuVo> skuVoList = productUtils.getSkuByProductId(productId);
        return ApiResult.ok(productUtils.getSpecData(product,skuVoList));
    }
}
