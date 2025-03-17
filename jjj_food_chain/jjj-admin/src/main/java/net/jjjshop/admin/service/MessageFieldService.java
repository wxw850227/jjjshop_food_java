package net.jjjshop.admin.service;

import net.jjjshop.admin.param.FieldParam;
import net.jjjshop.common.entity.settings.MessageField;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 应用消息字段表 服务类
 *
 * @author jjjshop
 * @since 2022-06-24
 */
public interface MessageFieldService extends BaseService<MessageField> {
    /**
     * 获取所有消息字段
     * @return
     */
    List<MessageField> getAll(Integer messageId);

    /**
     * 保存字段
     * @param fieldParam
     * @return
     */
    Boolean saveField(FieldParam fieldParam);

}
