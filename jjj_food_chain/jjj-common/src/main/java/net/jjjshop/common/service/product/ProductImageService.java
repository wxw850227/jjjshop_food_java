package net.jjjshop.common.service.product;

import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.vo.product.ProductImageVo;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 商品图片记录表 服务类
 *
 * @author jjjshop
 * @since 2022-06-28
 */
public interface ProductImageService extends BaseService<ProductImage> {
    /**
     * 根据id和图片类型获取图片
     * @param productId  商品id
     * @param imageType  图片类型
     * @return
     */
    List<ProductImageVo> getListByProductId(Integer productId, Integer imageType);
}
