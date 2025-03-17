package net.jjjshop.common.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.vo.order.OrderProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * 订单商品记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-04
 */
@Repository
public interface OrderProductMapper extends BaseMapper<OrderProduct> {
    List<OrderProductVo> getProductData(@Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate,
                                        @Param("payStatus") Integer payStatus);
}
