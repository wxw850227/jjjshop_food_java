package net.jjjshop.common.service.product.impl;

import net.jjjshop.common.entity.product.ProductFeed;
import net.jjjshop.common.mapper.product.ProductFeedMapper;
import net.jjjshop.common.service.product.ProductFeedService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 加料库 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class ProductFeedServiceImpl extends BaseServiceImpl<ProductFeedMapper, ProductFeed> implements ProductFeedService {

    @Autowired
    private ProductFeedMapper productFeedMapper;

}
