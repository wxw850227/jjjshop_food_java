package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.ProductAttribute;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.common.param.product.AttrParam;
import net.jjjshop.common.vo.product.ProductAttrVo;

/**
 * 属性库 服务类
 *
 * @author jjjfood
 * @since 2023-12-18
 */
public interface ProductAttributeService extends BaseService<ProductAttribute> {

    Paging<ProductAttrVo> getList(BasePageOrderParam param);

    boolean add(AttrParam param);

    boolean edit(AttrParam param);

    boolean delById(Integer attributeId);

    //批量删除
    boolean deletes(String attributeIds);
}
