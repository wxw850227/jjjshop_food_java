package net.jjjshop.shop.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.*;
import net.jjjshop.common.mapper.user.UserMapper;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.user.UserPageParam;
import net.jjjshop.shop.param.user.UserParam;
import net.jjjshop.shop.service.user.*;
import net.jjjshop.shop.vo.user.UserVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-01
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private UserUtils userUtils;

    /**
     * 查找用户
     * @param userPageParam
     * @return
     */
    public Paging<UserVo> getList(UserPageParam userPageParam) {
        // 用户列表
        Page<User> page = new PageInfo<>(userPageParam);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getIsDelete, 0);
        if(StringUtils.isNotEmpty(userPageParam.getNickName())){
            wrapper.like(User::getNickName, userPageParam.getNickName());
        }
        if(StringUtils.isNotEmpty(userPageParam.getMobile())){
            wrapper.like(User::getMobile, userPageParam.getMobile());
        }
        if(CollectionUtils.isNotEmpty(userPageParam.getRegDate()) && userPageParam.getRegDate().size() == 2){
            wrapper.between(User::getCreateTime, userPageParam.getRegDate().get(0) + " 00:00:00",
                    userPageParam.getRegDate().get(1) + " 23:59:59");
        }
        wrapper.orderByDesc(User::getCreateTime);
        IPage<User> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<UserVo> resultPage = iPage.convert(item -> {
            UserVo vo = new UserVo();
            BeanUtil.copyProperties(item, vo);
            return vo;
        });
        return new Paging(resultPage);

    }

    /**
     * 获取所有用户
     * @param
     * @return
     */
    public List<UserVo> getAll(){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0));
        List<UserVo> result = list.stream().map(e -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取所有用户Id
     * @param
     * @return
     */
    public List<Integer> getUserIds(){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0).orderByAsc(User::getUserId));
        List<Integer> result = list.stream().map(e -> {
            return e.getUserId();
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 修改用户余额
     * @param userParam
     * @return
     */
    public Boolean recharge(UserParam userParam) {
        if (userParam.getSource() == 0) {
            return this.rechargeToBalance(userParam);
        }
        return false;
    }


    /**
     * 软删除用户
     * @param userId
     * @return
     */
    public Boolean setDelete(Integer userId) {
        // 软删除,登录账号置空
        return this.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId)
                .set(User::getMobile, "")
                .set(User::getOpenId, "")
                .set(User::getMpopenId, "")
                .set(User::getAppopenId, "")
                .set(User::getIsDelete, 1));
    }

    /**
     * 修改用户余额
     * @param userParam
     * @return
     */
    private Boolean rechargeToBalance(UserParam userParam) {
        User user = this.getById(userParam.getUserId());
        BigDecimal balance = user.getBalance();
        BigDecimal money = userParam.getMoney();
        if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("请输入正确的金额");
        }
        // 判断充值方式，计算最终金额
        if ("inc".equals(userParam.getBalanceMode())) {
            balance = user.getBalance().add(money);
        } else if ("dec".equals(userParam.getBalanceMode())) {
            balance = balance.subtract(money).compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : balance.subtract(money);
            money = money.negate();
        } else {
            balance = money;
        }
        // 更新账户余额
        user.setBalance(balance);
        this.updateById(user);
        // 更新记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setScene(30);
        userBalanceLog.setUserId(userParam.getUserId());
        userBalanceLog.setMoney(money);
        userBalanceLog.setRemark(userParam.getBalanceRemark());
        userBalanceLog.setDescription("后台管理员 ["+ ShopLoginUtil.getUsername() +"] 操作");
        return userBalanceLogService.save(userBalanceLog);
    }

    /**
     * 获取用户统计数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    //获取订单统计数据
    public Integer getUserData(String startDate, String endDate, String type) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //开始查询时间不为空
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(User::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        //结束查询时间不为空
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(User::getCreateTime, DateUtil.parse(endDate+" 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            //如果结束查询时间为空,开始查询时间不为空，就默认设置时间查询区间为开始时间+1天
            wrapper.le(User::getCreateTime,DateUtil.parse(startDate+" 23:59:59"));
        }
        wrapper.eq(User::getIsDelete, 0);
        //根据查询模式返回不同的数值
        if ("user_total".equals(type)||"user_add".equals(type)) {
            return this.count(wrapper);
        } else if ("user_pay".equals(type)) {
            wrapper.gt(User::getPayMoney,0);
            return this.count(wrapper);
        } else if ("user_no_pay".equals(type)) {
            wrapper.eq(User::getPayMoney,0);
           return this.count(wrapper);
        }
        return 0;
    }
}
