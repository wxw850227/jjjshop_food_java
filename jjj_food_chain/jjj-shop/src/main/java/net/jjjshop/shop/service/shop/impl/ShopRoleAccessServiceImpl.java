package net.jjjshop.shop.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopRoleAccess;
import net.jjjshop.common.mapper.shop.ShopRoleAccessMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.shop.ShopRoleAccessService;
import org.springframework.stereotype.Service;

/**
 * 商家用户角色权限关系表 服务实现类
 * @author jjjshop
 * @since 2022-07-09
 */
@Slf4j
@Service
public class ShopRoleAccessServiceImpl extends BaseServiceImpl<ShopRoleAccessMapper, ShopRoleAccess> implements ShopRoleAccessService {
}
