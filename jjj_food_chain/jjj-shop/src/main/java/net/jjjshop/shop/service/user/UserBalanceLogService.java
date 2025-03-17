package net.jjjshop.shop.service.user;

import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.UserBalanceLogPageParam;
import net.jjjshop.shop.vo.user.UserBalanceLogVo;

/**
 * 用户余额变动明细表 服务类
 * @author jjjshop
 * @since 2022-07-21
 */
public interface UserBalanceLogService extends BaseService<UserBalanceLog> {

    /**
     * 分页查询用户余额明细
     * @param userBalanceLogPageParam
     * @return
     */
    Paging<UserBalanceLogVo> getList(UserBalanceLogPageParam userBalanceLogPageParam);
}
