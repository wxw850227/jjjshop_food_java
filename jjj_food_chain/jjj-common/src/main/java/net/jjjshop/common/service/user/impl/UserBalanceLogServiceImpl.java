package net.jjjshop.common.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.mapper.user.UserBalanceLogMapper;
import net.jjjshop.common.service.user.UserBalanceLogService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户余额变动明细表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserBalanceLogServiceImpl extends BaseServiceImpl<UserBalanceLogMapper, UserBalanceLog> implements UserBalanceLogService {

}
