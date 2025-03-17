package net.jjjshop.shop.service.user.impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.mapper.user.UserBalanceLogMapper;
import net.jjjshop.shop.param.user.UserBalanceLogPageParam;
import net.jjjshop.shop.service.user.UserBalanceLogService;
import net.jjjshop.shop.vo.user.UserBalanceLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户余额变动明细表 服务实现类
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserBalanceLogServiceImpl extends BaseServiceImpl<UserBalanceLogMapper, UserBalanceLog> implements UserBalanceLogService {

    @Autowired
    private UserBalanceLogMapper userBalanceLogMapper;

    /**
     * 分页查询用户余额明细
     * @param userBalanceLogPageParam
     * @return
     */
    public Paging<UserBalanceLogVo> getList(UserBalanceLogPageParam userBalanceLogPageParam) {
        // 开始页码
        userBalanceLogPageParam.setStartIndex((userBalanceLogPageParam.getPageIndex() - 1) * userBalanceLogPageParam.getPageSize());

        if(userBalanceLogPageParam.getValue1() != null && userBalanceLogPageParam.getValue1().size() == 2){
            userBalanceLogPageParam.setStartDate(userBalanceLogPageParam.getValue1().get(0) + " 00:00:00");
            userBalanceLogPageParam.setEndDate(userBalanceLogPageParam.getValue1().get(1) + " 23:59:59");
        }
        // 查询当前页列表
        List<UserBalanceLogVo> list = userBalanceLogMapper.getList(userBalanceLogPageParam);
        list.stream().forEach( e -> {
            e.setSceneText(BalanceLogEnum.getName(e.getScene()));
        });
        // 将结果封装Paging对象输出
        PageInfo<UserBalanceLogVo> pageInfo = new PageInfo<>();
        pageInfo.setRecords(list);
        pageInfo.setTotal(userBalanceLogMapper.getTotalCount(userBalanceLogPageParam));
        return new Paging<>(pageInfo);
    }



}
