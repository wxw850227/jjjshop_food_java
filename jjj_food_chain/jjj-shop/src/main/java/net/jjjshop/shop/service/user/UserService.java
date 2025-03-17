package net.jjjshop.shop.service.user;

import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.UserPageParam;
import net.jjjshop.shop.param.user.UserParam;
import net.jjjshop.shop.vo.user.UserVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 用户记录表 服务类
 * @author jjjshop
 * @since 2022-07-01
 */
public interface UserService extends BaseService<User> {
    /**
     * 查找用户
     * @param userPageParam
     * @return
     */
    Paging<UserVo> getList(UserPageParam userPageParam);

    /**
     * 获取所有用户
     * @param
     * @return
     */
    List<UserVo> getAll();


    /**
     * 修改用户余额
     * @param userParam
     * @return
     */
    Boolean recharge(UserParam userParam);

    /**
     * 软删除用户
     * @param userId
     * @return
     */
    Boolean setDelete(Integer userId);

    /**
     * 获取用户统计数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    Integer getUserData(String startDate, String endDate, String type);
}
