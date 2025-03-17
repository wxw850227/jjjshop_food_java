package net.jjjshop.shop.service.table.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.table.TableArea;
import net.jjjshop.common.entity.table.TableType;
import net.jjjshop.common.mapper.table.TableAreaMapper;
import net.jjjshop.common.param.table.TableAreaParam;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.service.table.TableAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<TableArea> getList() {
        LambdaQueryWrapper<TableArea> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableArea::getShopSupplierId, ShopLoginUtil.getShopSupplierId());
        wrapper.orderByAsc(TableArea::getSort);
        wrapper.orderByDesc(TableArea::getCreateTime);
        List<TableArea> list = this.list(wrapper);
        return list;
    }

    @Override
    public boolean add(TableAreaParam param) {
        TableArea bean = new TableArea();
        BeanUtils.copyProperties(param,bean);
        bean.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        return this.save(bean);
    }

    @Override
    public boolean edit(TableAreaParam param) {
        TableArea bean = new TableArea();
        BeanUtils.copyProperties(param,bean);
        return this.updateById(bean);
    }

    @Override
    public boolean delById(Integer areaId) {
        return this.removeById(areaId);
    }
}
