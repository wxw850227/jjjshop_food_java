package net.jjjshop.shop.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.shop.param.user.UserBalanceLogPageParam;
import net.jjjshop.shop.vo.user.UserBalanceLogVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户余额变动明细表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Repository
public interface UserBalanceLogMapper extends BaseMapper<UserBalanceLog> {

    /**
     * 通过昵称,变动场景,日期区间查询用户余额明细
     * @param userBalanceLogPageParam
     * @return
     */
    List<UserBalanceLogVo> getList(UserBalanceLogPageParam userBalanceLogPageParam);

    /**
     * 通过昵称查询用户余额变动明细数量
     * @param userBalanceLogPageParam
     * @return
     */
    Long getTotalCount(UserBalanceLogPageParam userBalanceLogPageParam);
}
