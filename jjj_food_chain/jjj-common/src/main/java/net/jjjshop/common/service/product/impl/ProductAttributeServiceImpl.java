package net.jjjshop.common.service.product.impl;

import net.jjjshop.common.entity.product.ProductAttribute;
import net.jjjshop.common.mapper.product.ProductAttributeMapper;
import net.jjjshop.common.service.product.ProductAttributeService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 属性库 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-18
 */
@Slf4j
@Service
public class ProductAttributeServiceImpl extends BaseServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

}
