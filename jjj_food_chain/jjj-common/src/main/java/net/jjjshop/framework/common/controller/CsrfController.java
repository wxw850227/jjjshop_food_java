

package net.jjjshop.framework.common.controller;

import net.jjjshop.framework.log.annotation.OperationLogIgnore;
import net.jjjshop.framework.util.UUIDUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * CSRF 供swagger调用
 **/
@ApiIgnore
@OperationLogIgnore
@RestController
public class CsrfController {

    @RequestMapping(value = "/csrf", method = {RequestMethod.GET, RequestMethod.POST})
    public String csrf() {
        return UUIDUtil.getUuid();
    }

}
