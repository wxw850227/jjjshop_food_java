

package net.jjjshop.shop.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.mapper.shop.ShopAccessMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.shop.ShopAccessService;
import org.springframework.stereotype.Service;


/**
 * 系统用户 服务实现类
 */
@Slf4j
@Service
public class ShopAccessServiceImpl extends BaseServiceImpl<ShopAccessMapper, ShopAccess> implements ShopAccessService {
}
