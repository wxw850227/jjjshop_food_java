package net.jjjshop.common.util.message;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.factory.sms.impl.AliyunSmsFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class SmsMessageUtils {
    @Autowired
    private AliyunSmsFactoryService aliyunSmsFactoryService;
    /**
     * 发送订阅消息
     */
    public void send(JSONObject data, String template, String mobile, Long appId)
    {
        try{
            // 组装参数
            JSONObject params = JSONObject.parseObject(template);
            JSONObject varData = params.getJSONObject("varData");
            JSONObject templateParam = new JSONObject();
            Iterator iter = varData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                JSONObject value = varData.getJSONObject(key);
                // 如果模板包含变量
                if(data.containsKey(key)){
                    templateParam.put(value.getString("fieldName"), data.getString(key));
                }else{
                    templateParam.put(key, value.getString("fieldValue"));
                }
            }
            aliyunSmsFactoryService.sendSms(mobile, params.getString("templateId"), templateParam, appId);
        }catch (Exception e){
            log.info("短信消息发送失败:",e);
        }
    }
}
