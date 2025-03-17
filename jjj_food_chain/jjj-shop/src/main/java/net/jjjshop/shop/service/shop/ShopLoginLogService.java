package net.jjjshop.shop.service.shop;

import net.jjjshop.common.entity.shop.ShopLoginLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.shop.LoginLogPageParam;
import net.jjjshop.shop.vo.shop.LoginLogVo;

/**
 * 管理员登录记录表 服务类
 * @author jjjshop
 * @since 2022-08-15
 */
public interface ShopLoginLogService extends BaseService<ShopLoginLog> {

    /**
     * 获取所有登陆记录
     * @param loginLogPageParam
     * @return
     */
    Paging<LoginLogVo> getList(LoginLogPageParam loginLogPageParam);

}
