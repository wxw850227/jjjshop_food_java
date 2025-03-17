package net.jjjshop.front.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.mapper.order.OrderProductMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.order.OrderProductService;
import net.jjjshop.front.vo.order.OrderProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单商品记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderProductServiceImpl extends BaseServiceImpl<OrderProductMapper, OrderProduct> implements OrderProductService {

    @Autowired
    private UploadFileUtils uploadFileUtils;


    /**
     * 通过订单id查找商品
     * @param orderId
     * @return
     */
    public List<OrderProduct> getProductList(Integer orderId) {
        return this.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, orderId));
    }

    /**
     * 通过订单id查找商品vo
     * @param orderId
     * @return
     */
    public List<OrderProductVo> getProductVoList(Integer orderId) {
        return this.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, orderId))
                .stream().map(e -> {
                    OrderProductVo vo = new OrderProductVo();
                    BeanUtils.copyProperties(e, vo);
                    vo.setProductImage(uploadFileUtils.getFilePath(vo.getImageId()));
                    return vo;
                }).collect(Collectors.toList());
    }

}
