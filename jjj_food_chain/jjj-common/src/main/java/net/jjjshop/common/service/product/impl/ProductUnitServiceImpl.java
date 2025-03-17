package net.jjjshop.common.service.product.impl;

import net.jjjshop.common.entity.product.ProductUnit;
import net.jjjshop.common.mapper.product.ProductUnitMapper;
import net.jjjshop.common.service.product.ProductUnitService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 单位库 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class ProductUnitServiceImpl extends BaseServiceImpl<ProductUnitMapper, ProductUnit> implements ProductUnitService {

    @Autowired
    private ProductUnitMapper productUnitMapper;

}
