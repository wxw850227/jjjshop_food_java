package net.jjjshop.shop.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.MessageField;
import net.jjjshop.common.mapper.settings.MessageFieldMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.settings.MessageFieldService;
import org.springframework.stereotype.Service;

/**
 * 应用消息字段表 服务实现类
 * @author jjjshop
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageFieldServiceImpl extends BaseServiceImpl<MessageFieldMapper, MessageField> implements MessageFieldService {
}
