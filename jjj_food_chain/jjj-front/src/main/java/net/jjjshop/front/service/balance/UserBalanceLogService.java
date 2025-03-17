package net.jjjshop.front.service.balance;

import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.balance.UserBalanceLogPageParam;
import net.jjjshop.front.vo.balance.UserBalanceLogVo;

import java.util.List;

/**
 * 用户余额变动明细表 服务类
 * @author jjjshop
 * @since 2022-07-21
 */
public interface UserBalanceLogService extends BaseService<UserBalanceLog> {

    /**
     * 查询前十条用户余额明细
     * @param user
     * @return
     */
    List<UserBalanceLogVo> getTop10(User user);

    /**
     * 分页查询用户余额明细
     * @param userBalanceLogPageParam
     * @param user
     * @return
     */
    Paging<UserBalanceLogVo> getList(UserBalanceLogPageParam userBalanceLogPageParam, User user);
}
