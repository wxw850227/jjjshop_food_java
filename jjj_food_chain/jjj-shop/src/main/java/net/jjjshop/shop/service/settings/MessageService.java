package net.jjjshop.shop.service.settings;

import com.alibaba.fastjson.JSONObject;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.settings.vo.GetPhoneVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.setting.MessageSettingsParam;
import net.jjjshop.shop.vo.setting.MessageVo;

import java.util.List;
import java.util.Map;

/**
 * 应用消息表 服务实现类
 * @author jjjshop
 * @since 2022-06-24
 */
public interface MessageService extends BaseService<Message> {
    /**
     * 获取所有消息设置
     * @return
     */
    List<MessageVo> getAll(Integer messageTo);

    /**
     * 获取字段信息
     * @param messageId
     * @param messageType
     * @return
     */
    Map<String, Object> getField(Integer messageId, String messageType);

    /**
     * 保存配置
     * @param messageSettingsParam
     * @return
     */
    Boolean saveSettings(MessageSettingsParam messageSettingsParam);

    /**
     * 修改启用状态
     * @param messageSettingsId
     * @param messageType
     * @return
     */
    Boolean updateSettingsStatus(Integer messageSettingsId, String messageType);

    //获取页面类型
    List<JSONObject> getAllType(GetPhoneVo getPhoneVo);
}
