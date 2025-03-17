package net.jjjshop.common.service.table.impl;

import net.jjjshop.common.entity.table.TableArea;
import net.jjjshop.common.mapper.table.TableAreaMapper;
import net.jjjshop.common.service.table.TableAreaService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 桌位区域管理 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class TableAreaServiceImpl extends BaseServiceImpl<TableAreaMapper, TableArea> implements TableAreaService {

    @Autowired
    private TableAreaMapper tableAreaMapper;

}
