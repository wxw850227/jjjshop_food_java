package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.order.UserCart;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户购物车 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-08-02
 */
@Repository
public interface UserCartMapper extends BaseMapper<UserCart> {
}
