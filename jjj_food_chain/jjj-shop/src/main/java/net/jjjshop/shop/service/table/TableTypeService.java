package net.jjjshop.shop.service.table;

import net.jjjshop.common.entity.table.TableType;
import net.jjjshop.common.param.table.TableTypeParam;
import net.jjjshop.common.vo.store.table.TableTypeVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;

import java.util.List;

/**
 * 桌位类型 服务类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
public interface TableTypeService extends BaseService<TableType> {

    List<TableType> getList();

    boolean add(TableTypeParam param);

    boolean edit(TableTypeParam param);

    boolean delById(Integer typeId);
}
