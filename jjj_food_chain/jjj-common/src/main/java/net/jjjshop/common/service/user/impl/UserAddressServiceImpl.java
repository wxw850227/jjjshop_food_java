package net.jjjshop.common.service.user.impl;

import net.jjjshop.common.entity.user.UserAddress;
import net.jjjshop.common.mapper.user.UserAddressMapper;
import net.jjjshop.common.service.user.UserAddressService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户收货地址表 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */
@Slf4j
@Service
public class UserAddressServiceImpl extends BaseServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

}
