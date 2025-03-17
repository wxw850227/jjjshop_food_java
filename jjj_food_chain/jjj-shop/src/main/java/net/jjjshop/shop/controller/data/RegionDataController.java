package net.jjjshop.shop.controller.data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.RegionCache;
import net.jjjshop.common.vo.RegionVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "查询用户接口", tags = {"查询用户接口"})
@RestController
@RequestMapping("/shop/data/region")
public class RegionDataController {

    @Autowired
    private RegionCache regionCache;


    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<List<RegionVo>> index() throws Exception{
        return ApiResult.ok(regionCache.getCacheTree());
    }
}
