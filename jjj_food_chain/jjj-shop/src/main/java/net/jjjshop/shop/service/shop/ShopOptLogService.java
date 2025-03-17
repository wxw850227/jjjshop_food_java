package net.jjjshop.shop.service.shop;

import net.jjjshop.common.entity.shop.ShopOptLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.shop.OptLogPageParam;
import net.jjjshop.shop.vo.shop.OptLogVo;

/**
 * 管理员操作记录表 服务类
 * @author jjjshop
 * @since 2022-08-15
 */
public interface ShopOptLogService extends BaseService<ShopOptLog> {

    /**
     * 获取所有操作记录
     * @param optLogPageParam
     * @return
     */
    Paging<OptLogVo> getList(OptLogPageParam optLogPageParam);

}
