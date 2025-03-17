package net.jjjshop.common.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.order.OrderAddress;

import org.springframework.stereotype.Repository;


/**
 * 订单收货地址记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-04
 */
@Repository
public interface OrderAddressMapper extends BaseMapper<OrderAddress> {


}
