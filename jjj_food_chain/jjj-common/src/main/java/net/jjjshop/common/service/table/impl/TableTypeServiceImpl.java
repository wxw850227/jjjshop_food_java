package net.jjjshop.common.service.table.impl;

import net.jjjshop.common.entity.table.TableType;
import net.jjjshop.common.mapper.table.TableTypeMapper;
import net.jjjshop.common.service.table.TableTypeService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 桌位类型 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class TableTypeServiceImpl extends BaseServiceImpl<TableTypeMapper, TableType> implements TableTypeService {

    @Autowired
    private TableTypeMapper tableTypeMapper;

}
