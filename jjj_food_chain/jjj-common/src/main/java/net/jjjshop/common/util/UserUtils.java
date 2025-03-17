package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class UserUtils {

    @Autowired
    private UserService userService;

    /**
     * 累积用户的可用积分
     */
    public void setIncPoints(Integer userId, BigDecimal points, String description, Boolean upgrade)
    {
        User user = userService.getById(userId);
        // 更新用户可用积分
        BigDecimal userPoints = BigDecimal.ZERO;
        if((user.getPoints().add(points)).compareTo(BigDecimal.ZERO) > 0){
            userPoints = user.getPoints().add(points);
        }
        userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId)
                .set(User::getPoints, userPoints));
        // 用户总积分
        if(points.compareTo(BigDecimal.ZERO) > 0){
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId)
                    .setSql("`total_points` = `total_points` + " + points));
        }
    }

    /**
     * 累积用户总消费金额
     */
    public Boolean setIncPayMoney(User user, BigDecimal money) {
        user.setPayMoney(user.getPayMoney().add(money));
        return userService.updateById(user);
    }

    /**
     * 提现驳回：冻结用户资金
     */
    public Boolean freezeMoney(User user, BigDecimal money) {
        user.setBalance(user.getBalance().subtract(money));
        user.setFreezeMoney(user.getFreezeMoney().add(money));
        return userService.updateById(user);
    }

    /**
     * 提现打款成功：累积提现余额
     */
    public Boolean totalMoney(Integer userId, BigDecimal money) {
        User user = userService.getById(userId);
        user.setFreezeMoney(user.getFreezeMoney().subtract(money));
        user.setCashMoney(user.getCashMoney().add(money));
        return userService.updateById(user);
    }

    /**
     * 提现驳回：解冻用户资金
     */
    public Boolean backFreezeMoney(User user, BigDecimal money) {
        user.setBalance(user.getBalance().add(money));
        user.setFreezeMoney(user.getFreezeMoney().subtract(money));
        return userService.updateById(user);
    }
}
