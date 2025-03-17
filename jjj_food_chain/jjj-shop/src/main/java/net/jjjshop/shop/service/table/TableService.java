package net.jjjshop.shop.service.table;

import net.jjjshop.common.entity.table.Table;
import net.jjjshop.common.param.table.TableParam;
import net.jjjshop.common.vo.product.ProductAttrVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.table.TablePageParam;

/**
 * 桌位管理 服务类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
public interface TableService extends BaseService<Table> {

    Paging<Table> index(TablePageParam param);

    boolean add(TableParam param);

    boolean edit(TableParam param);

    boolean delById(Integer tableId);
}
