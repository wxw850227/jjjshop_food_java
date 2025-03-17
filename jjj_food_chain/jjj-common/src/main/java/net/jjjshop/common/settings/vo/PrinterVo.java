package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("打印机设置VO")
public class PrinterVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //是否开启厨房小票打印
    private Integer roomOpen;
    //打印机ID
    private Integer roomPrinterId;
    //是否开启顾客小票打印
    private Integer buyerOpen;
    private Integer buyerPrinterId;
    //是否开启商家小票打印
    private Integer sellerOpen;
    private Integer sellerPrinterId;

    public PrinterVo() {
        this.roomOpen = 0;
        this.roomPrinterId = 0;
        this.buyerOpen = 0;
        this.buyerPrinterId = 0;
        this.sellerOpen = 0;
        this.sellerPrinterId = 0;
    }

}
