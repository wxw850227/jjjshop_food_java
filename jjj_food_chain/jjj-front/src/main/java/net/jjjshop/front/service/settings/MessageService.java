package net.jjjshop.front.service.settings;

import net.jjjshop.common.entity.settings.Message;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 应用消息表 服务实现类
 * @author jjjshop
 * @since 2022-06-24
 */
public interface MessageService extends BaseService<Message> {
    /**
     * 获取消息
     */
    List<String> getMessageByNameArr(String platform, String[] messageENameArr);

    //是否获取手机号
    Boolean isGetPhone(User user);
}
