package net.jjjshop.common.util.message;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.util.wx.AppWxUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WxMessageUtils {
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    @Lazy
    @Autowired
    private AppWxUtils appWxUtils;
    /**
     * 发送订阅消息
     */
    public void send(JSONObject data, String template, String openId, Long appId)
    {
        try{
            // 组装参数
            JSONObject params = JSONObject.parseObject(template);
            JSONObject varData = params.getJSONObject("varData");
            List<WxMaSubscribeMessage.MsgData> msgDataList = new ArrayList<>();
            Iterator iter = varData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                JSONObject value = varData.getJSONObject(key);
                // 如果模板包含变量
                if(data.containsKey(key)){
                    msgDataList.add(new WxMaSubscribeMessage.MsgData(value.getString("fieldName"), getValueStr(data.getString(key))));
                }else{
                    msgDataList.add(new WxMaSubscribeMessage.MsgData(value.getString("fieldName"), getValueStr(value.getString("fieldValue"))));
                }
            }
            WxMaSubscribeMessage message = new WxMaSubscribeMessage();
            message.setData(msgDataList);
            message.setPage("/pages/index/index");
            message.setToUser(openId);
            message.setTemplateId(params.getString("templateId"));
            wxMaService.switchoverTo(appWxUtils.getConfig(wxMaService, appId))
                    .getMsgService().sendSubscribeMsg(message);
        }catch (Exception e){
            log.info("微信小程序消息发送失败:",e);
        }
    }

    /**
     * 截取值，不能超过20字符
     * @return
     */
    private String getValueStr(String value){
        if(StringUtils.isEmpty(value)){
            return " ";
        }
        if(value.length() > 20){
            return value.substring(0,20);
        }
        return value;
    }
}
