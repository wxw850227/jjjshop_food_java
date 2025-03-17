package net.jjjshop.shop.service.app;

import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 微信小程序记录表 服务类
 *
 * @author jjjshop
 * @since 2022-07-03
 */
public interface AppWxService extends BaseService<AppWx> {

    /**
     * 修改
     * @param wxappId
     * @param wxappSecret
     * @return
     */
    Boolean edit(String wxappId, String wxappSecret);
}
