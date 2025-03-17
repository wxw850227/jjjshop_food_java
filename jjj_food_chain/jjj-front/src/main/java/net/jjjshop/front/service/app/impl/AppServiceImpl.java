package net.jjjshop.front.service.app.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.PlatformEnum;
import net.jjjshop.common.mapper.app.AppMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.util.LoginUtil;
import net.jjjshop.front.service.app.AppService;
import net.jjjshop.front.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信小程序记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-23
 */
@Slf4j
@Service
public class AppServiceImpl extends BaseServiceImpl<AppMapper, App> implements AppService {

    @Autowired
    private UserService userService;

    /**
     * 支付方式
     * @param paySource
     * @return
     */
    public List<Integer> getPayType(String paySource){
        App app = this.getOne(new LambdaQueryWrapper<>());
        List<Integer> result = new ArrayList<>();
        if(StringUtils.isNotEmpty(app.getPayType())){
            JSONObject json = JSON.parseObject(app.getPayType());
            JSONArray payType = json.getJSONObject(paySource).getJSONArray("payType");
            for (int i=0;i<payType.size();i++){
                result.add(payType.getIntValue(i));
            }
        }else{
            // 获取默认
            result = PlatformEnum.getPayType(paySource);
        }
        if("wx".equals(paySource)){
            User user = userService.getById(LoginUtil.getUserId());
            //判断是否有微信openid,没有则移除微信支付
            if(StringUtils.isEmpty(user.getOpenId())){
                Integer weixin = 20;
                result.remove(weixin);
            }
        }
        if("mp".equals(paySource)){
            User user = userService.getById(LoginUtil.getUserId());
            //判断是否有微信公众号openid,没有则移除微信支付
            if(StringUtils.isEmpty(user.getMpopenId())){
                Integer weixin = 20;
                result.remove(weixin);
            }
        }
        return result;
    }
}
