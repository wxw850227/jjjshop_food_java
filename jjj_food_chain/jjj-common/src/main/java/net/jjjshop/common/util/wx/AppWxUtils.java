package net.jjjshop.common.util.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.service.app.AppWxService;
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
@Configuration
public class AppWxUtils {

    @Lazy
    @Autowired
    private AppWxService appWxService;

    @Bean
    public WxMaConfig wxMaConfig() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        // 临时放入，支付会再次设置
        config.setAppid("jjjshop");
        config.setSecret("jjjshop");
        return config;
    }


    @Bean
    public WxMaService wxMaService(WxMaConfig maConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.addConfig(maConfig.getAppid(), maConfig);
        return service;
    }

    public String getConfig(WxMaService wxMaService, Long appId){
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        AppWx appWx = appWxService.getById(appId);
        if(appWx == null || StringUtils.isEmpty(appWx.getWxappId())){
            throw new BusinessException("未设置微信小程序参数");
        }
        try{
            wxMaService.switchoverTo(appWx.getWxappId());
        }catch (Exception e) {
            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
            config.setAppid(appWx.getWxappId());
            config.setSecret(appWx.getWxappSecret());
            wxMaService.addConfig(appWx.getWxappId(), config);
        }
        return appWx.getWxappId();
    }
}
