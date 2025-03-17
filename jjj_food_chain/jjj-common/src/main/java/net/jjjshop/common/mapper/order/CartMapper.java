package net.jjjshop.common.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.order.UserCart;

import org.springframework.stereotype.Repository;


/**
 * 购物车 Mapper 接口
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Repository
public interface CartMapper extends BaseMapper<UserCart> {


}
