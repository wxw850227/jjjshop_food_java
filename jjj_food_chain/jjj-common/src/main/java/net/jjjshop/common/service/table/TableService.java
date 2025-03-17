package net.jjjshop.common.service.table;

import net.jjjshop.common.entity.table.Table;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 桌位管理 服务类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
public interface TableService extends BaseService<Table> {

    String getName(Integer tableId);
}
