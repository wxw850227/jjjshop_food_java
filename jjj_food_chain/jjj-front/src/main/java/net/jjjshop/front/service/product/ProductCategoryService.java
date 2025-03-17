package net.jjjshop.front.service.product;

import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.param.order.CategoryListParam;

import java.util.List;

/**
 * 产品分类表 服务类
 * @author jjjshop
 * @since 2022-06-28
 */
public interface ProductCategoryService extends BaseService<ProductCategory> {

    List<CategoryVo> getList(CategoryListParam param);

}
