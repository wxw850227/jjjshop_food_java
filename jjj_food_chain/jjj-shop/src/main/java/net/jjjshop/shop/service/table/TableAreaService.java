package net.jjjshop.shop.service.table;

import net.jjjshop.common.entity.table.TableArea;
import net.jjjshop.common.param.table.TableAreaParam;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 桌位区域管理 服务类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
public interface TableAreaService extends BaseService<TableArea> {

    List<TableArea> getList();

    boolean add(TableAreaParam param);

    boolean edit(TableAreaParam param);

    boolean delById(Integer areaId);
}
