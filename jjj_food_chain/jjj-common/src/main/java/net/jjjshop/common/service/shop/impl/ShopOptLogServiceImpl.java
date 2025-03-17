package net.jjjshop.common.service.shop.impl;

import net.jjjshop.common.entity.shop.ShopOptLog;
import net.jjjshop.common.mapper.shop.ShopOptLogMapper;
import net.jjjshop.common.service.shop.ShopOptLogService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 管理员操作记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopOptLogServiceImpl extends BaseServiceImpl<ShopOptLogMapper, ShopOptLog> implements ShopOptLogService {

    @Autowired
    private ShopOptLogMapper shopOptLogMapper;

}
