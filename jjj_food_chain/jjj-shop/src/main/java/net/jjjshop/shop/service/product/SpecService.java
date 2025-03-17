package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.Spec;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.product.SpecParam;

/**
 * 商品规格组记录表 服务类
 *
 * @author jjjfood
 * @since 2023-12-18
 */
public interface SpecService extends BaseService<Spec> {

    Paging<Spec> getList(CommonPageParam param);

    boolean add(SpecParam param);

    boolean edit(SpecParam param);

    boolean delById(Integer specId);

    boolean deletes(String specIds);
}
