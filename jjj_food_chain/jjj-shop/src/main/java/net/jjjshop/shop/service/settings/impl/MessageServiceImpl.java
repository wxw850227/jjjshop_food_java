package net.jjjshop.shop.service.settings.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.entity.settings.MessageField;
import net.jjjshop.common.entity.settings.MessageSettings;
import net.jjjshop.common.enums.GetPhoneTypeEnum;
import net.jjjshop.common.enums.MessageTypeEnum;
import net.jjjshop.common.mapper.settings.MessageMapper;
import net.jjjshop.common.settings.vo.GetPhoneVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.param.setting.MessageSettingsParam;
import net.jjjshop.shop.service.settings.MessageFieldService;
import net.jjjshop.shop.service.settings.MessageService;
import net.jjjshop.shop.service.settings.MessageSettingsService;
import net.jjjshop.shop.vo.setting.MessageVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private MessageFieldService messageFieldService;
    /**
     * 获取所有消息设置
     * @param messageTo
     * @return
     */
    public List<MessageVo> getAll(Integer messageTo){
        List<Message> list = this.list(new LambdaQueryWrapper<Message>()
                .eq(Message::getIsDelete, false).eq(Message::getMessageTo, messageTo));
        return list.stream().map(e -> {
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(e, messageVo);
            messageVo.setMessageTypeText(MessageTypeEnum.getName(messageVo.getMessageType()));
            MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                    .eq(MessageSettings::getMessageId, messageVo.getMessageId()));
            if(settings != null){
                messageVo.setMpStatus(settings.getMpStatus());
                messageVo.setWxStatus(settings.getWxStatus());
                messageVo.setSmsStatus(settings.getSmsStatus());
                messageVo.setMessageSettingsId(settings.getMessageSettingsId());
                messageVo.setMpTemplate(settings.getMpTemplate());
                messageVo.setWxTemplate(settings.getWxTemplate());
                messageVo.setSmsTemplate(settings.getSmsTemplate());
            }else{
                messageVo.setMpStatus(0);
                messageVo.setWxStatus(0);
                messageVo.setSmsStatus(0);
            }
            return messageVo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取字段信息
     * @param messageId
     * @param messageType
     * @return
     */
    public Map<String, Object> getField(Integer messageId, String messageType){
        Map<String, Object> result = new HashMap<>();
        List<MessageField> list = messageFieldService.list(new LambdaQueryWrapper<MessageField>()
                .eq(MessageField::getMessageId, messageId).eq(MessageField::getIsDelete, false)
                .orderByAsc(MessageField::getSort));
        MessageSettings settings = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                .eq(MessageSettings::getMessageId, messageId));
        if(settings == null){
            result.put("settings", null);
        } else {
            if("mp".equals(messageType)){
                result.put("settings", JSONObject.parseObject(settings.getMpTemplate()));
            }else if("wx".equals(messageType)){
                result.put("settings", JSONObject.parseObject(settings.getWxTemplate()));
            }else if("sms".equals(messageType)){
                result.put("settings", JSONObject.parseObject(settings.getSmsTemplate()));
            }
        }
        result.put("list", list);
        return result;
    }

    /**
     * 保存配置
     * @param messageSettingsParam
     * @return
     */

    public Boolean saveSettings(MessageSettingsParam messageSettingsParam){
        boolean isUpdate = false;
        MessageSettings settings = new MessageSettings();
        settings.setMessageId(messageSettingsParam.getMessageId());
        // 如果存在则修改
        MessageSettings settingsBean = messageSettingsService.getOne(new LambdaQueryWrapper<MessageSettings>()
                .eq(MessageSettings::getMessageId, messageSettingsParam.getMessageId()));
        if(settingsBean != null){
            isUpdate = true;
            settings.setMessageSettingsId(settingsBean.getMessageSettingsId());
        }
        JSONObject varData = new JSONObject();
        for(MessageSettingsParam.FieldParam filed: messageSettingsParam.getFieldList()){
            JSONObject objField = new JSONObject();
            objField.put("fieldName", filed.getFieldNewEname());
            objField.put("fieldValue", filed.getFiledNewValue());
            varData.put(filed.getFieldEname(), objField);
        }
        if("mp".equals(messageSettingsParam.getMessageType())){
            JSONObject mpData = new JSONObject();
            mpData.put("templateId", messageSettingsParam.getTemplateId());
            mpData.put("varData", varData);
            settings.setMpStatus(1);
            settings.setMessageId(messageSettingsParam.getMessageId());
            settings.setMpTemplate(mpData.toJSONString());
        }else if("wx".equals(messageSettingsParam.getMessageType())){
            JSONObject wxData = new JSONObject();
            wxData.put("templateId", messageSettingsParam.getTemplateId());
            wxData.put("varData", varData);
            settings.setWxStatus(1);
            settings.setMessageId(messageSettingsParam.getMessageId());
            settings.setWxTemplate(wxData.toJSONString());
        }else if("sms".equals(messageSettingsParam.getMessageType())){
            JSONObject smsData = new JSONObject();
            smsData.put("templateId", messageSettingsParam.getTemplateId());
            smsData.put("varData", varData);
            settings.setSmsStatus(1);
            settings.setMessageId(messageSettingsParam.getMessageId());
            settings.setSmsTemplate(smsData.toJSONString());
        }
        if(isUpdate){
            return messageSettingsService.updateById(settings);
        }else{
            return messageSettingsService.save(settings);
        }
    }

    /**
     * 修改启用状态
     * @param messageSettingsId
     * @param messageType
     * @return
     */
    public Boolean updateSettingsStatus(Integer messageSettingsId, String messageType){
        MessageSettings settings = new MessageSettings();
        MessageSettings oldBean = messageSettingsService.getById(messageSettingsId);
        settings.setMessageSettingsId(messageSettingsId);
        if("mp".equals(messageType)){
            settings.setMpStatus(oldBean.getMpStatus() == 0?1:0);
        } else if("wx".equals(messageType)){
            settings.setWxStatus(oldBean.getWxStatus() == 0?1:0);
        } else if("sms".equals(messageType)){
            settings.setSmsStatus(oldBean.getSmsStatus() == 0?1:0);
        }else {
            return false;
        }
        return messageSettingsService.updateById(settings);
    }

    //获取页面类型
    @Override
    public List<JSONObject> getAllType(GetPhoneVo getPhoneVo) {
        List<JSONObject> allType = new ArrayList<>();
        if(CollectionUtils.isEmpty(getPhoneVo.getCheckedCities())){
            return allType;
        }
        for(Integer id : getPhoneVo.getCheckedCities()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("name", GetPhoneTypeEnum.getName(id));
            allType.add(jsonObject);
        }
        return allType;
    }
}
