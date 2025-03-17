package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.User;

import org.springframework.stereotype.Repository;


/**
 * 用户记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-01
 */
@Repository
public interface UserMapper extends BaseMapper<User> {


}
