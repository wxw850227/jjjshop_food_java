package net.jjjshop.shop.controller.data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.Page;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.service.page.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "链接数据", tags = {"链接数据"})
@RestController
@RequestMapping("/shop/data/link")
public class LinkDataController {

    @Autowired
    private PageService pageService;

    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @OperationLog(name = "getPageList")
    @ApiOperation(value = "getPageList", response = String.class)
    public ApiResult<List<Page>> getPageList() throws Exception{
        return ApiResult.ok(pageService.getAll(20));
    }
}
