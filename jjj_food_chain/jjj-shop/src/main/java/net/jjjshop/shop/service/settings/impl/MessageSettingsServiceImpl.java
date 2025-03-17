package net.jjjshop.shop.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.MessageSettings;
import net.jjjshop.common.mapper.settings.MessageSettingsMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.settings.MessageSettingsService;
import org.springframework.stereotype.Service;

/**
 * 应用消息设置表 服务实现类
 * @author jjjshop
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageSettingsServiceImpl extends BaseServiceImpl<MessageSettingsMapper, MessageSettings> implements MessageSettingsService {
}
