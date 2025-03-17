package net.jjjshop.shop.service.order;

import net.jjjshop.shop.vo.order.OrderVo;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 订单导出 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface ExportService {
    /**
     * 导出订单
     * @param orderList
     * @param httpServletResponse
     * @return
     */
    void orderList(List<OrderVo> orderList, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException;
}
