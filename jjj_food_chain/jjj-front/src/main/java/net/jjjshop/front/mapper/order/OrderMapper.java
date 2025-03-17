package net.jjjshop.front.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.order.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 订单记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-04
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 普通商品购买数量
     * @param userId
     * @param productId
     * @return
     */
    Integer getHasBuyOrderNum(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 活动商品购买数量
     * @param userId
     * @param productId
     * @return
     */
    Integer getPlusOrderNum(@Param("userId") Integer userId, @Param("productId") Integer productId,@Param("orderSource") Integer orderSource);
}
