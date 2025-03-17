package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserBalanceLog;
import org.springframework.stereotype.Repository;


/**
 * 用户余额变动明细表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Repository
public interface UserBalanceLogMapper extends BaseMapper<UserBalanceLog> {

}
