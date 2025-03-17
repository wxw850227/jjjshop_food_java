package net.jjjshop.front.service.app;

import net.jjjshop.common.entity.app.App;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 微信小程序记录表 服务类
 * @author jjjshop
 * @since 2022-06-23
 */
public interface AppService extends BaseService<App> {
    /**
     * 支付方式
     * @param paySource
     * @return
     */
    List<Integer> getPayType(String paySource);
}
