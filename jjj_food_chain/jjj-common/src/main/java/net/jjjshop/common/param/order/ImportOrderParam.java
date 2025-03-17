package net.jjjshop.common.param.order;

import lombok.Data;
import net.jjjshop.common.util.excel.ExcelImport;

@Data
public class ImportOrderParam {
    private static final long serialVersionUID = 1L;

    @ExcelImport(value = "订单编号")
    private String orderNo;

    @ExcelImport(value = "物流公司")
    private String expressName;

    @ExcelImport(value = "物流单号")
    private String expressNo;

    //错误提示
    private String rowTips;

}


