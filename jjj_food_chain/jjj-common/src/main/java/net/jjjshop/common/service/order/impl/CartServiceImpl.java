package net.jjjshop.common.service.order.impl;

import net.jjjshop.common.entity.order.UserCart;
import net.jjjshop.common.mapper.order.CartMapper;
import net.jjjshop.common.service.order.CartService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 购物车 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class CartServiceImpl extends BaseServiceImpl<CartMapper, UserCart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

}
