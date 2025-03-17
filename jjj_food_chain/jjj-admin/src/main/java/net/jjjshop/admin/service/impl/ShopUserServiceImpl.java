package net.jjjshop.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.service.ShopUserService;
import net.jjjshop.common.entity.shop.ShopUser;
import net.jjjshop.common.mapper.shop.ShopUserMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商家用户记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-23
 */
@Slf4j
@Service
public class ShopUserServiceImpl extends BaseServiceImpl<ShopUserMapper, ShopUser> implements ShopUserService {


}
