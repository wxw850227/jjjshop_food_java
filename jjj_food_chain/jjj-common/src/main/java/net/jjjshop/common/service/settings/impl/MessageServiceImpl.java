package net.jjjshop.common.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.mapper.settings.MessageMapper;
import net.jjjshop.common.service.settings.MessageService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 应用消息表 服务实现类
 * @author jjjshop
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements MessageService {

}
