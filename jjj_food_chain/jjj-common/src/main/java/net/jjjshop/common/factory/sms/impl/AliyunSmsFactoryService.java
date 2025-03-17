package net.jjjshop.common.factory.sms.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.sms.SmsFactoryService;
import net.jjjshop.common.settings.vo.SmsVo;
import net.jjjshop.common.util.SettingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 阿里云短信工厂类
 */
@Slf4j
@Service
public class AliyunSmsFactoryService extends SmsFactoryService {
    @Autowired
    private SettingUtils settingUtils;

    /**
     * 更新商品库存 (针对下单减库存的商品)
     */
    public Boolean sendSms(String mobile, String templateCode, JSONObject templateParamJson,Long shopSupplierId){
        String templateParam = templateParamJson.toJSONString();
        // 短信设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.SMS.getKey(), shopSupplierId);
        SmsVo smsVo = JSONObject.toJavaObject(vo, SmsVo.class);
        //AK
        String accessKeyId = smsVo.getAliyunSms().getAccessKeyId();
        String accessKeySecret = smsVo.getAliyunSms().getAccessKeySecret();
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsVo.getAliyunSms().getSign());
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        //request.setTemplateParam("{\"code\":\"988756\"}");
        request.setTemplateParam(templateParam);
        try{
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        }catch (Exception e){
            log.info("短信发送失败", e);
            return false;
        }
        return true;
    }
}
