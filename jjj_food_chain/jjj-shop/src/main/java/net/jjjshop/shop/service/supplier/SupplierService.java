package net.jjjshop.shop.service.supplier;

import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.supplier.SupplierPageParam;
import net.jjjshop.shop.param.supplier.SupplierParam;
import net.jjjshop.shop.param.supplier.SupplierSettingParam;
import net.jjjshop.shop.vo.supplier.SupplierVo;

import java.math.BigDecimal;

/**
 * 供应商表 服务类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
public interface SupplierService extends BaseService<Supplier> {
    /**
     * 商户分页列表
     * @param supplierPageParam
     * @return
     */
    Paging<SupplierVo> getList(SupplierPageParam supplierPageParam);

    /**
     * 获取编辑参数
     * @param id
     * @return
     */
    SupplierVo toEdit(Integer id);

    /**
     * 编辑供应商
     * @param supplierParam
     * @return
     */
    Boolean edit(SupplierParam supplierParam);

    SupplierVo editDetail(Integer shopSupplierId);

    boolean setting(SupplierSettingParam supplierParam);

    Integer getSupplierTotalByDay(String today);
}
