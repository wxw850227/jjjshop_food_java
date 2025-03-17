package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("打印机设置VO")
public class PrintingVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer isOpen;
    private Integer orderStatus;
    private Integer printerId;

    public PrintingVo() {
        this.isOpen = 1;
        this.orderStatus = 20;
        this.printerId = 0;
    }

}
