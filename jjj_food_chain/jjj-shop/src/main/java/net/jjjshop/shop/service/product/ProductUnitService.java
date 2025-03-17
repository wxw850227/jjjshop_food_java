package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.ProductUnit;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.product.UnitParam;

/**
 * 单位库 服务类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
public interface ProductUnitService extends BaseService<ProductUnit> {

    Paging<ProductUnit> getList(CommonPageParam param);

    boolean add(UnitParam param);

    boolean edit(UnitParam param);

    boolean delById(Integer unitId);

    boolean deletes(String unitIds);
}
