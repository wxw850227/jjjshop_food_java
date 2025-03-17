package net.jjjshop.common.service.shop.impl;

import net.jjjshop.common.entity.shop.ShopLoginLog;
import net.jjjshop.common.mapper.shop.ShopLoginLogMapper;
import net.jjjshop.common.service.shop.ShopLoginLogService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 管理员登录记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopLoginLogServiceImpl extends BaseServiceImpl<ShopLoginLogMapper, ShopLoginLog> implements ShopLoginLogService {

    @Autowired
    private ShopLoginLogMapper shopLoginLogMapper;

}
