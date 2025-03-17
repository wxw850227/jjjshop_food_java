package net.jjjshop.shop.service.statistics.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.statistics.UserRankingService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.user.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户统计数据 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */

@Slf4j
@Service
public class UserRankingServiceImpl implements UserRankingService {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * 获取用户统计数据
     * @param
     * @return
     */
    public JSONObject getData() throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");

        Integer userTotalToday = userService.getUserData(null, null, "user_total");
        Integer userTotalYesterday = userService.getUserData(null, null, "user_total");
        Integer userAddToday = userService.getUserData(today, null, "user_add");
        Integer userAddYesterday = userService.getUserData(yesterday, null, "user_add");
        BigDecimal userPayToday = orderService.getOrderData(today, null, "order_user_total",0,-1);
        BigDecimal userPayYesterday = orderService.getOrderData(yesterday, null, "order_user_total",0,-1);
        JSONObject json = new JSONObject();
        json.put("userTotalToday", userTotalToday);
        json.put("userTotalYesterday", userTotalYesterday);
        json.put("userAddToday", userAddToday);
        json.put("userAddYesterday", userAddYesterday);
        json.put("userPayToday", Integer.parseInt(userPayToday.toString()));
        json.put("userPayYesterday", Integer.parseInt(userPayYesterday.toString()));
        return json;
    }

    /**
     * 获取用户排行
     * @param type
     * @return
     */
    public List<UserVo> getUserRanking(String type) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId, User::getNickName, User::getAvatarUrl, User::getExpendMoney, User::getTotalPoints, User::getTotalInvite);
        wrapper.eq(User::getIsDelete, 0).last("LIMIT 10");
        if ("pay".equals(type)) {
            wrapper.orderByDesc(User::getExpendMoney);
        } else if ("points".equals(type)) {
            wrapper.orderByDesc(User::getTotalPoints);
        } else if ("invite".equals(type)) {
            wrapper.orderByDesc(User::getTotalInvite);
        }
        List<User> list = userService.list(wrapper);
        List<UserVo> result = list.stream().map(e -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 通过时间范围获取已付款和未付款用户数
     * @param day
     * @return
     */
    public JSONObject getPayScaleData(Integer day) throws ParseException {
        //获取今天时间
        Date today = DateUtil.offsetDay(new Date(), 0);
        if (day == 1) {
            day = 0;
        }
        String startDate = DateUtil.format(DateUtil.offsetDay(today, -day), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.offsetDay(today, 0), "yyyy-MM-dd");
        Integer pay = userService.getUserData(startDate, endDate, "user_pay");
        Integer noPay = userService.getUserData(startDate, endDate, "user_no_pay");
        JSONObject json = new JSONObject();
        json.put("pay", pay);
        json.put("no_pay", noPay);
        return json;
    }

    /**
     * 通过时间范围获取新用户统计数据
     * @param startDate,endDate
     * @return
     */
    public Map<String, Object> getNewUserByDate(String startDate, String endDate) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        //endTime加一天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t, 1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            BigDecimal totalNum = new BigDecimal(userService.getUserData(day, null, "user_add"));
            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("totalNum", totalNum);
            data.add(json);
            days.add(day);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }

    /**
     * 通过时间范围获取付款用户统计数据
     * @param startDate,endDate
     * @return
     */
    public Map<String, Object> getPayUserByDate(String startDate, String endDate) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        DateTime startTime = DateUtil.parse(startDate);
        DateTime endTime = DateUtil.parse(endDate);
        //endTime加1天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t, 1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            BigDecimal totalNum = orderService.getOrderData(day, null, "order_user_total",0,-1);
            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("totalNum", Integer.parseInt(totalNum.toString()));
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }

    /**
     * 获取用户总数
     * @param
     * @return
     */
    public Integer getUserTotal() {
        return userService.count(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0));
    }
}
