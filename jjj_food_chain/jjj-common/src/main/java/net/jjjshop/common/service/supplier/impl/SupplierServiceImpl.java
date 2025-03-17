package net.jjjshop.common.service.supplier.impl;

import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.mapper.supplier.SupplierMapper;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 供应商表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Slf4j
@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

}
