package net.jjjshop.shop.service.shop.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopUserRole;
import net.jjjshop.common.mapper.shop.ShopUserRoleMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.shop.ShopUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 商家用户角色记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-09
 */
@Slf4j
@Service
public class ShopUserRoleServiceImpl extends BaseServiceImpl<ShopUserRoleMapper, ShopUserRole> implements ShopUserRoleService {
}
