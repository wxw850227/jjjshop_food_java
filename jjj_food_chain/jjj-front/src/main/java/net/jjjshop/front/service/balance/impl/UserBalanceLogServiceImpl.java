package net.jjjshop.front.service.balance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.common.mapper.user.UserBalanceLogMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.balance.UserBalanceLogPageParam;
import net.jjjshop.front.service.balance.UserBalanceLogService;
import net.jjjshop.front.vo.balance.UserBalanceLogVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户余额变动明细表 服务实现类
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserBalanceLogServiceImpl extends BaseServiceImpl<UserBalanceLogMapper, UserBalanceLog> implements UserBalanceLogService {

    /**
     * 查询前十条用户余额明细
     * @param user
     * @return
     */
    public List<UserBalanceLogVo> getTop10(User user) {
        List<UserBalanceLog> list = this.list(new LambdaQueryWrapper<UserBalanceLog>()
                .eq(UserBalanceLog::getUserId, user.getUserId())
                .orderByDesc(UserBalanceLog::getCreateTime)
                .last("LIMIT 0,10"));
        return list.stream().map(e -> {
            UserBalanceLogVo vo = new UserBalanceLogVo();
            BeanUtils.copyProperties(e, vo);
            vo.setSceneText(BalanceLogEnum.getName(vo.getScene()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 分页查询用户余额明细
     * @param userBalanceLogPageParam
     * @param user
     * @return
     */
    public Paging<UserBalanceLogVo> getList(UserBalanceLogPageParam userBalanceLogPageParam, User user) {
        Page<UserBalanceLog> page = new PageInfo<>();
        LambdaQueryWrapper<UserBalanceLog> wrapper = new LambdaQueryWrapper<UserBalanceLog>()
                .eq(UserBalanceLog::getUserId, user.getUserId())
                .orderByDesc(UserBalanceLog::getCreateTime);
        if ("rechange".equals(userBalanceLogPageParam.getType())) {
            wrapper.eq(UserBalanceLog::getScene, 10);
        }
        IPage<UserBalanceLog> iPage = this.page(page, wrapper);
        IPage<UserBalanceLogVo> result = iPage.convert(item -> {
            UserBalanceLogVo vo = new UserBalanceLogVo();
            BeanUtils.copyProperties(item, vo);
            vo.setSceneText(BalanceLogEnum.getName(vo.getScene()));
            return vo;
        });
        return new Paging(result);
    }


}
