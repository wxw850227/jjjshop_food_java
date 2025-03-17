package net.jjjshop.shop.service.table.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductAttribute;
import net.jjjshop.common.entity.table.TableType;
import net.jjjshop.common.mapper.table.TableTypeMapper;
import net.jjjshop.common.param.table.TableTypeParam;
import net.jjjshop.common.vo.product.ProductAttrVo;
import net.jjjshop.common.vo.store.table.TableTypeVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.service.table.TableTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<TableType> getList() {
        LambdaQueryWrapper<TableType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableType::getShopSupplierId, ShopLoginUtil.getShopSupplierId());
        wrapper.orderByAsc(TableType::getSort);
        wrapper.orderByDesc(TableType::getCreateTime);
        List<TableType> list = this.list(wrapper);
        return list;
    }

    @Override
    public boolean add(TableTypeParam param) {
        TableType bean = new TableType();
        BeanUtils.copyProperties(param,bean);
        bean.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        return this.save(bean);
    }

    @Override
    public boolean edit(TableTypeParam param) {
        TableType bean = new TableType();
        BeanUtils.copyProperties(param,bean);
        return this.updateById(bean);
    }

    @Override
    public boolean delById(Integer typeId) {
        return this.removeById(typeId);
    }
}
