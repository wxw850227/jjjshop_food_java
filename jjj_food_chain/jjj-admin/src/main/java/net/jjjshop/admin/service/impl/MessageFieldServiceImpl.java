package net.jjjshop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.param.FieldParam;
import net.jjjshop.admin.service.MessageFieldService;
import net.jjjshop.common.entity.settings.MessageField;
import net.jjjshop.common.mapper.settings.MessageFieldMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用消息字段表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-24
 */
@Slf4j
@Service
public class MessageFieldServiceImpl extends BaseServiceImpl<MessageFieldMapper, MessageField> implements MessageFieldService {

    @Autowired
    private MessageFieldMapper messageFieldMapper;

    public List<MessageField> getAll(Integer messageId){
        LambdaQueryWrapper<MessageField> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MessageField::getMessageId, messageId);
        wrapper.eq(MessageField::getIsDelete, false);
        wrapper.orderByAsc(MessageField::getSort);
        // 获取所有已启用的权限列表
        return this.list(wrapper);
    }
    /**
     * 保存字段
     * @param fieldParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveField(FieldParam fieldParam){
        //先删除字段
        LambdaQueryWrapper<MessageField> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(MessageField::getMessageId, fieldParam.getMessageId());

        MessageField deleteField = new MessageField();
        deleteField.setIsDelete(true);
        update(deleteField, lambdaQueryWrapper);

        //再增加字段
        List<MessageField> list = new ArrayList<MessageField>();
        for(MessageField bean:fieldParam.getFieldData()){
            MessageField addField = new MessageField();
            BeanUtils.copyProperties(bean, addField);
            if(bean.getMessageId() == null){
                addField.setMessageId(fieldParam.getMessageId());
            }
            list.add(addField);
        }
        return this.saveBatch(list);
    }

}
