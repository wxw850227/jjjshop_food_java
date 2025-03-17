package net.jjjshop.shop.service.statistics;

import com.alibaba.fastjson.JSONObject;
import net.jjjshop.shop.vo.user.UserVo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 用户统计数据 服务类
 * @author jjjshop
 * @since 2022-06-28
 */

public interface UserRankingService {

    /**
     * 获取用户统计数据
     * @param
     * @return
     */
    JSONObject getData() throws ParseException;

    /**
     * 获取用户排行
     * @param type
     * @return
     */
    List<UserVo> getUserRanking(String type);

    /**
     * 通过时间范围获取已付款和未付款用户数
     * @param day
     * @return
     */
    JSONObject getPayScaleData(Integer day) throws ParseException;

    /**
     * 通过时间范围获取新用户统计数据
     * @param startDate,endDate
     * @return
     */
    Map<String, Object> getNewUserByDate(String startDate, String endDate) throws ParseException;

    /**
     * 通过时间范围获取付款用户统计数据
     * @param startDate,endDate
     * @return
     */
    Map<String, Object> getPayUserByDate(String startDate, String endDate) throws ParseException;

    /**
     * 获取用户总数
     * @param
     * @return
     */
    Integer getUserTotal();
}
