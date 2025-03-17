package net.jjjshop.common.service.table.impl;

import net.jjjshop.common.entity.table.Table;
import net.jjjshop.common.mapper.table.TableMapper;
import net.jjjshop.common.service.table.TableService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 桌位管理 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class TableServiceImpl extends BaseServiceImpl<TableMapper, Table> implements TableService {

    @Autowired
    private TableMapper tableMapper;

    @Override
    public String getName(Integer tableId) {
        Table table = this.getById(tableId);
        if(table != null){
            return table.getTableNo();
        }
        return "";
    }
}
