package net.jjjshop.shop.controller.statistics;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.statistics.RankingParam;
import net.jjjshop.shop.service.statistics.OrderRankingService;
import net.jjjshop.shop.service.statistics.ProductRankingService;
import net.jjjshop.shop.service.statistics.UserRankingService;
import net.jjjshop.shop.vo.product.ProductVo;
import net.jjjshop.shop.vo.statistics.ProductSaleRankingVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/statistics/sales")
public class SalesController {

    @Autowired
    private ProductRankingService productRankingService;

    @Autowired
    private OrderRankingService orderRankingService;

    @Autowired
    private UserRankingService userRankingService;


    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @OperationLog(name = "order")
    @ApiOperation(value = "order", response = String.class)
    public ApiResult<Map<String,Object>> order(@Validated @RequestBody RankingParam rankingParam) throws Exception{
        return ApiResult.ok(orderRankingService.getOrderDataByDate(rankingParam));
    }

}
