

package net.jjjshop.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.param.FieldParam;
import net.jjjshop.admin.param.MessageParam;
import net.jjjshop.admin.service.MessageFieldService;
import net.jjjshop.admin.service.MessageService;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.entity.settings.MessageField;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "user", tags = {"user"})
@RestController
@RequestMapping("/admin/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageFieldService messageFieldService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<Message>> index() {
        return ApiResult.ok(messageService.getAll());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer id) {
        if(messageService.setDelete(id)) {
            return ApiResult.ok(null, "删除成功");
        }else{
            return ApiResult.fail("删除失败");
        }
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated MessageParam messageParam) {
        if(messageService.add(messageParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated MessageParam messageParam) {
        if(messageService.edit(messageParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/field", method = RequestMethod.POST)
    @OperationLog(name = "field")
    @ApiOperation(value = "field", response = String.class)
    public ApiResult<List<MessageField>> field(Integer messageId) {
        return ApiResult.ok(messageFieldService.getAll(messageId));
    }

    @RequestMapping(value = "/saveField", method = RequestMethod.POST)
    @OperationLog(name = "saveField")
    @ApiOperation(value = "saveField", response = String.class)
    public ApiResult<String> saveField(@RequestBody @Validated FieldParam fieldParam) {
        if(messageFieldService.saveField(fieldParam)) {
            return ApiResult.ok(null, "保存成功");
        }else{
            return ApiResult.fail("保存失败");
        }
    }
}
