package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.vo.order.OrderProductVo;
import net.jjjshop.shop.service.order.ExportService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.vo.order.OrderExportVo;
import net.jjjshop.shop.vo.order.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单导出 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private OrderService orderService;

    /**
     * 导出订单
     * @param orderList
     * @param httpServletResponse
     * @return
     */
    public void orderList(List<OrderVo> orderList, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {
        //转换为导出对象
        List<OrderExportVo> list = orderList.stream().map(e -> {
            OrderExportVo vo = new OrderExportVo();
            BeanUtils.copyProperties(e, vo);
            //设置商品信息
            vo.setProduct(this.filterProductInfo(e));
            vo.setIsCommentText(e.getIsComment() == 1 ? "是" : "否");
            return vo;
        }).collect(Collectors.toList());


        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("orderNo", "订单编号");
        writer.addHeaderAlias("product", "商品信息");
        writer.addHeaderAlias("totalPrice", "订单总额");
        writer.addHeaderAlias("couponMoney", "优惠券抵扣");
        writer.addHeaderAlias("pointsMoney", "积分抵扣");
        writer.addHeaderAlias("expressPrice", "运费金额");
        writer.addHeaderAlias("updatePriceSymbol", "后台改价");
        writer.addHeaderAlias("payPrice", "实付款金额");
        writer.addHeaderAlias("payTypeText", "支付方式");
        writer.addHeaderAlias("createTime", "下单时间");
        writer.addHeaderAlias("nickname", "买家");
        writer.addHeaderAlias("buyerRemark", "买家留言");
        writer.addHeaderAlias("deliveryTypeText", "配送方式");
        writer.addHeaderAlias("extractStoreName", "自提门店名称");
        writer.addHeaderAlias("extractLinkman", "自提联系人");
        writer.addHeaderAlias("extractPhone", "自提联系电话");
        writer.addHeaderAlias("addressName", "收货人姓名");
        writer.addHeaderAlias("addressPhone", "联系电话");
        writer.addHeaderAlias("detailRegion", "收货人地址");
        writer.addHeaderAlias("expressName", "物流公司");
        writer.addHeaderAlias("expressNo", "物流单号");
        writer.addHeaderAlias("payStatusText", "付款状态");
        writer.addHeaderAlias("payTime", "付款时间");
        writer.addHeaderAlias("deliveryStatusText", "发货状态");
        writer.addHeaderAlias("deliveryTime", "发货时间");
        writer.addHeaderAlias("receiptStatusText", "收货状态");
        writer.addHeaderAlias("receiptTime", "收货时间");
        writer.addHeaderAlias("orderStatusText", "订单状态");
        writer.addHeaderAlias("transactionId", "微信支付交易号");
        writer.addHeaderAlias("isCommentText", "是否已评价");

        writer.write(list, true);

        httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
        //name是下载对话框的名称，不支持中文，想用中文名称需要进行utf8编码
        String excelName = "订单-" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
        excelName = URLEncoder.encode(excelName, "utf-8");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls");

        //设置返回excel的格式为xlsx
        //httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xlsx");

        ServletOutputStream excelOut = null;
        //将excel文件信息写入输出流，返回给调用者
        try {
            excelOut = httpServletResponse.getOutputStream();
            writer.flush(excelOut, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }

        IoUtil.close(excelOut);

    }

    /**
     * 转换订单商品信息
     * @param
     * @return
     */
    private String filterProductInfo(OrderVo orderVo) {
        StringBuilder str = new StringBuilder();
        orderVo.getProduct().stream().forEach(e -> {
            Iterator<OrderProductVo> iterator = orderVo.getProduct().listIterator();
            str.append("商品名称:").append(e.getProductName()).append(" ");
            if (StringUtils.isNotEmpty(e.getProductAttr())) {
                str.append("商品规格:").append(e.getProductAttr()).append(" ");
            }
            str.append("购买数量:").append(e.getTotalNum()).append(" ");
            str.append("商品总价:").append(e.getTotalPrice());
            if (iterator.hasNext()) {
                str.append("\n");
            }
        });
        return str.toString();
    }


}
