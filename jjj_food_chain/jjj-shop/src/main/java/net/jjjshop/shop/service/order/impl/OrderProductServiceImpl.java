package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.mapper.order.OrderProductMapper;
import net.jjjshop.common.vo.order.OrderProductVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.order.OrderProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单商品记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderProductServiceImpl extends BaseServiceImpl<OrderProductMapper, OrderProduct> implements OrderProductService {

    @Autowired
    private OrderProductMapper orderProductMapper;

    /**
     * 按照时间范围获取商品订单数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    public int getOrderProductData(String startDate, String endDate, String type) {
        LambdaQueryWrapper<OrderProduct> wrapper = new LambdaQueryWrapper<>();
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotEmpty(startDate)) {
            startTime = DateUtil.parse(startDate + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(endDate)) {
            endTime = DateUtil.parse(endDate + " 23:59:59");
        } else if (StringUtils.isNotEmpty(startDate)) {
            endTime = DateUtil.parse(startDate + " 23:59:59");
        }
        int t = 0;
        if ("no_pay".equals(type)) {
            t = 10;
        } else if ("pay".equals(type)) {
            t = 20;
        }
        List<OrderProductVo> productData = orderProductMapper.getProductData(startTime, endTime, t);
        return productData.size();
    }
}
