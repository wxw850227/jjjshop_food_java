package net.jjjshop.shop.vo.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderDeliver;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.util.excel.ExcelExport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("外卖配送导出VO")
public class ExportDeliverVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelExport("订单号")
    private String orderNo;

    @ExcelExport("订单金额")
    private BigDecimal payPrice;

    @ExcelExport("配送费")
    private BigDecimal price;

    @ApiModelProperty("订单状态(10进行中 20被取消 30已完成)")
    @ExcelExport("订单状态")
    private String statusText;

    @ApiModelProperty("配送状态(待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5)")
    @ExcelExport("配送状态")
    private String deliverStatusText;

    @ExcelExport("配送方式")
    private String deliverSourceText;

    @ExcelExport("骑手姓名")
    private String linkman;

    @ExcelExport("骑手电话")
    private String phone;

}
