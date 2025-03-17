package net.jjjshop.shop.service.app.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.mapper.app.AppWxMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.jjjshop.shop.service.app.AppWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 微信小程序记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-03
 */
@Slf4j
@Service
public class AppWxServiceImpl extends BaseServiceImpl<AppWxMapper, AppWx> implements AppWxService {
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    /**
     * 修改
     * @param wxappId
     * @param wxappSecret
     * @return
     */
    public Boolean edit(String wxappId, String wxappSecret){
        AppWx app = this.getOne(new LambdaQueryWrapper<>());
        AppWx updateBean = new AppWx();
        updateBean.setWxappId(wxappId);
        updateBean.setWxappSecret(wxappSecret);
        // 删除缓存
        try{
            wxMaService.removeConfig(wxappId);
            // 旧的也删除
            if(!wxappId.equals(app.getWxappId())){
                wxMaService.removeConfig(app.getWxappId());
            }
        }catch (Exception e){
            log.info("保存设置删除缓存错误");
        }
        if(app == null){
            return this.save(updateBean);
        }else{
            return this.update(updateBean, new LambdaQueryWrapper<>());
        }
    }
}
