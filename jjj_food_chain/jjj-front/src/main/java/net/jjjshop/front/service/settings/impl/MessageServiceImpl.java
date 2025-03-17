package net.jjjshop.front.service.settings.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.entity.settings.MessageSettings;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.GetPhoneTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.mapper.settings.MessageMapper;
import net.jjjshop.common.settings.vo.GetPhoneVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.settings.MessageService;
import net.jjjshop.front.service.settings.MessageSettingsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 应用消息表 服务实现类
 * @author jjjshop
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements MessageService {
    @Autowired
    private MessageSettingsService messageSettingsService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取消息
     */
    public List<String> getMessageByNameArr(String platform, String[] messageENameArr){
        List<String> templateArr = new ArrayList<>();
        //只适用于微信
        if("wx".equals(platform)){
            List<Message> list = this.list(new LambdaQueryWrapper<Message>()
                    .eq(Message::getIsDelete, false).in(Message::getMessageEname, messageENameArr)
                    .orderByAsc(Message::getSort));
            for(Message message:list){
                MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                        .eq(MessageSettings::getMessageId, message.getMessageId()));
                if(settings != null && settings.getWxStatus() == 1 && StringUtils.isNotBlank(settings.getWxTemplate())){
                    templateArr.add(JSONObject.parseObject(settings.getWxTemplate()).getString("templateId"));
                }
            }
        }
        //只适用于公众号
        if("mp".equals(platform)){
            List<Message> list = this.list(new LambdaQueryWrapper<Message>()
                    .eq(Message::getIsDelete, false).in(Message::getMessageEname, messageENameArr)
                    .orderByAsc(Message::getSort));
            for(Message message:list){
                MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                        .eq(MessageSettings::getMessageId, message.getMessageId()));
                if(settings != null && settings.getMpStatus() == 1 && StringUtils.isNotBlank(settings.getMpTemplate())){
                    templateArr.add(JSONObject.parseObject(settings.getMpTemplate()).getString("templateId"));
                }
            }
        }

        return templateArr;
    }

    // 是否获取手机号
    @Override
    public Boolean isGetPhone(User user) {
        if(StringUtils.isNotEmpty(user.getMobile())){
            return false;
        }
        JSONObject vo = settingUtils.getSetting(SettingEnum.GETPHONE.getKey(), null);
        GetPhoneVo getPhoneVo = JSONObject.toJavaObject(vo, GetPhoneVo.class);
        List<Integer> checkedCities = getPhoneVo.getCheckedCities();
        if(CollectionUtils.isEmpty(checkedCities)){
            return false;
        }
        //获取页面类型
        List<Integer> list = GetPhoneTypeEnum.getValueList();
        if(list.containsAll(checkedCities)){
            //拒绝后几天不再提醒，设置为0则每次都要提醒
            Integer sendDay = getPhoneVo.getSendDay();
            if(sendDay <= 0){
                return true;
            }
            String key = "get_phone_";
            String cacheKey = String.format(CommonRedisKey.SETTING_DATA, user.getUserId(), key);
            Object object = redisTemplate.opsForValue().get(cacheKey);
            //是否已缓存
            if(object == null){
                // 存入缓存
                redisTemplate.opsForValue().set(cacheKey, user.getUserId(),sendDay, TimeUnit.DAYS);
                return true;
            }
        }
        return false;
    }

}
