package net.jjjshop.common.util.wx;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.v3.auth.Verifier;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.service.app.AppWxService;
import net.jjjshop.common.vo.WxPayResult;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 维信小程序工具类
 */
@Slf4j
@Configuration
public class WxPayUtils {

    @Lazy
    @Autowired
    private AppService appService;
    @Lazy
    @Autowired
    private AppWxService appWxService;

    @Bean
    public WxPayService wxPayService() {
        WxPayService wxPayService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId("jjjshop");
        wxPayService.addConfig("jjjshop","1", payConfig);
        return wxPayService;
    }

    public WxPayResult getConfig(WxPayService wxPayService, String paySource, Long appId){
        WxPayResult wxPayResult = new WxPayResult();
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        App app = appService.getById(appId);
        if(app == null || StringUtils.isEmpty(app.getMchid())){
            throw new BusinessException("未设置微信支付");
        }
        String mchIdKey = "";
        if ("mp".equals(paySource) || "h5".equals(paySource)) {
            mchIdKey = "mp";
        } else if ("wx".equals(paySource)) {
            mchIdKey = "wx";
        } else if ("android".equals(paySource) || "ios".equals(paySource)) {
            mchIdKey = "open";
        }
        mchIdKey = app.getMchid() + "_" + mchIdKey;
//        try{
//            wxPayService.switchoverTo(mchIdKey);
//        }catch (Exception e){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(StringUtils.trimToNull(app.getMchid()));
        payConfig.setMchKey(StringUtils.trimToNull(app.getApikey()));
        payConfig.setApiV3Key(StringUtils.trimToNull(app.getApikey()));
        payConfig.setSubAppId(null);
        payConfig.setSubMchId(null);
        payConfig.setKeyContent(app.getP12());
        payConfig.setPrivateKeyContent(app.getKeyPem()==null?null : app.getKeyPem().getBytes());
        payConfig.setPrivateCertContent(app.getCertPem()==null?null : app.getCertPem().getBytes());
        //支付验签类型,0证书,1公钥
        if(app.getWxSignType() != null && app.getWxSignType() == 1){
            //公钥ID
            payConfig.setPublicKeyId(StringUtils.trimToNull(app.getWechatpaySerial()));
            if(StringUtils.isBlank(app.getPubKeyPem())){
                throw new BusinessException("支付公钥pub_key不能为空");
            }
            //pub_key.pem证书文件内容的字节数组
            payConfig.setPublicKeyContent(app.getPubKeyPem().getBytes());
        }
            // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        String payAppId = "";
        AppWx appWx = appWxService.getById(appId);
        if(appWx != null){
            payAppId = StringUtils.trimToNull(appWx.getWxappId());
        }
        payConfig.setAppId(StringUtils.trimToNull(payAppId));
        wxPayService.addConfig(mchIdKey,payAppId, payConfig);
        if(app.getWxPayKind() == 3){
            if(StringUtils.isBlank(app.getWechatpaySerial())){
                throw new BusinessException("微信平台证书序列号不能为空");
            }
        }
        wxPayResult.setMchId(mchIdKey);
        wxPayResult.setAppId(payAppId);

//    }
        return wxPayResult;
    }

    public WxPayResult getWxPayResult(String paySource, Long appId) {
        WxPayResult wxPayResult = new WxPayResult();
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        App app = appService.getById(appId);
        if(app == null || StringUtils.isEmpty(app.getMchid())){
            throw new BusinessException("未设置微信支付商户号");
        }
        String payAppId = "";
        if ("wx".equals(paySource)) {
            AppWx appWx = appWxService.getById(appId);
            if(appWx != null){
                payAppId = StringUtils.trimToNull(appWx.getWxappId());
            }
        }
        wxPayResult.setMchId(app.getMchid());
        wxPayResult.setAppId(payAppId);
        return wxPayResult;
    }

}
