package net.jjjshop.shop.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.mapper.product.ProductImageMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.product.ProductImageService;
import org.springframework.stereotype.Service;

/**
 * 商品图片记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductImageServiceImpl extends BaseServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {
}
