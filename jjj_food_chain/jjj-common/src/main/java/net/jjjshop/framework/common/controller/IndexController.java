

package net.jjjshop.framework.common.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.log.annotation.OperationLogIgnore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 项目根路径提示信息
 */
@Slf4j
@Controller
@OperationLogIgnore
@Api(value = "Index API", tags = {"Index"})
public class IndexController {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }

    /**
     * SwaggerUI
     */
    @GetMapping("/docs")
    public String swagger() {
        return "redirect:/swagger-ui.html";
    }

}
