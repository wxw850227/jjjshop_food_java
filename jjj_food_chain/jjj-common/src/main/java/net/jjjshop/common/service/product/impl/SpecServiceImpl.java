package net.jjjshop.common.service.product.impl;

import net.jjjshop.common.entity.product.Spec;
import net.jjjshop.common.mapper.product.SpecMapper;
import net.jjjshop.common.service.product.SpecService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品规格组记录表 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-18
 */
@Slf4j
@Service
public class SpecServiceImpl extends BaseServiceImpl<SpecMapper, Spec> implements SpecService {

    @Autowired
    private SpecMapper specMapper;

}
