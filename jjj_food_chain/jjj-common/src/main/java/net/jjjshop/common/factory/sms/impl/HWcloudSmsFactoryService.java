package net.jjjshop.common.factory.sms.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.sms.SmsFactoryService;
import net.jjjshop.common.settings.vo.SmsVo;
import net.jjjshop.common.util.HuaWeiSMSUtil;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.vo.sms.HwSmsRoot;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 华为云短信工厂类
 */
@Slf4j
@Service
public class HWcloudSmsFactoryService extends SmsFactoryService {
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private HuaWeiSMSUtil huaWeiSMSUtil;


    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    public Boolean sendSms(String mobile, String templateCode, JSONObject templateParam,Long shopSupplierId)  {
        // 短信设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.SMS.getKey(), shopSupplierId);
        SmsVo smsVo = JSONObject.toJavaObject(vo, SmsVo.class);

        //必填,请参考"开发准备"获取如下数据,替换为实际值
        String appKey = smsVo.getHWcloudSms().getAccessKeyId(); //APP_Key
        String appSecret = smsVo.getHWcloudSms().getAccessKeySecret(); //APP_Secret
        String sender = smsVo.getHWcloudSms().getSender(); //国内短信签名通道号或国际/港澳台短信通道号
        String templateId = smsVo.getHWcloudSms().getTemplateCode(); //模板ID
        //APP接入地址 //(在控制台"应用管理"页面获取)+接口访问URI
        //北区 "https://smsapi.cn-north-4.myhuaweicloud.com:443"
        //南区 "https://smsapi.cn-south-1.myhuaweicloud.com:443"
        String url = smsVo.getHWcloudSms().getUrl(); //APP接入地址
        if(!url.contains("v1")){
            url = url + "/sms/batchSendSms/v1";
        }
        //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
        //国际/港澳台短信不用关注该参数
        String signature = smsVo.getHWcloudSms().getSign(); //签名名称

        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        String receiver = "+86"+mobile; //短信接收人号码

        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
        String statusCallBack = "";

        // 随机6位数验证码
        String templateParas = "[\""+templateParam.getString("code")+"\"]"; //模板变量，此处以单变量验证码短信为例，请客户自行生成6位验证码，并定义为字符串类型，以杜绝首位0丢失的问题（例如：002569变成了2569）。

        //请求Body,不携带签名名称时,signature请填null
        String body = huaWeiSMSUtil.buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty()) {
            return false;
        }

        try {
            //请求Headers中的X-WSSE参数值
            String wsseHeader = huaWeiSMSUtil.buildWsseHeader(appKey, appSecret);
            if (null == wsseHeader || wsseHeader.isEmpty()) {
                return false;
            }

            CloseableHttpClient client = HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                            (x509CertChain, authType) -> true).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            //请求方法POST
            HttpResponse response = client.execute(RequestBuilder.create("POST")
                    .setUri(url)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                    .addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());

            //打印响应消息实体
            String entity = EntityUtils.toString(response.getEntity());
            HwSmsRoot hwSmsRoot = JSONObject.parseObject(entity, HwSmsRoot.class);
            //000000代表请求成功
            if(!hwSmsRoot.getCode().equals("000000")){
                log.info("华为云短信发送失败,错误码:"+hwSmsRoot.getCode());
                return false;
            }
            log.info("华为云短信发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("华为云短信发送失败:"+e);
            return false;
        }

        return true;
    }
}
