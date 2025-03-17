package net.jjjshop.shop.service.table.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductAttribute;
import net.jjjshop.common.entity.table.Table;
import net.jjjshop.common.entity.table.TableArea;
import net.jjjshop.common.entity.table.TableType;
import net.jjjshop.common.mapper.table.TableMapper;
import net.jjjshop.common.param.table.TableParam;
import net.jjjshop.common.vo.product.ProductAttrVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.table.TablePageParam;
import net.jjjshop.shop.service.table.TableAreaService;
import net.jjjshop.shop.service.table.TableService;
import net.jjjshop.shop.service.table.TableTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

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
    @Autowired
    private TableAreaService tableAreaService;
    @Autowired
    private TableTypeService tableTypeService;

    @Override
    public Paging<Table> index(TablePageParam param) {
        Page<Table> page = new PageInfo<>(param);
        LambdaQueryWrapper<Table> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Table::getShopSupplierId, ShopLoginUtil.getShopSupplierId());
        wrapper.orderByAsc(Table::getSort);
        wrapper.orderByDesc(Table::getCreateTime);
        IPage<Table> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<Table> resultPage = iPage.convert(result -> {
            return transVo(result);
        });
        return new Paging(iPage);
    }

    public Table transVo(Table bean){
        TableArea area = tableAreaService.getById(bean.getAreaId());
        if(area != null){
            bean.setAreaName(area.getAreaName());
        }
        TableType tableType = tableTypeService.getById(bean.getTypeId());
        if(tableType != null){
            bean.setTypeName(tableType.getTypeName());
            bean.setMinNum(tableType.getMinNum());
            bean.setMaxNum(tableType.getMaxNum());
        }
        return bean;
    }

    @Override
    public boolean add(TableParam param) {
        Table bean = new Table();
        BeanUtils.copyProperties(param,bean);
        bean.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        TableArea area = tableAreaService.getById(bean.getAreaId());
        if(area != null){
            bean.setAreaName(area.getAreaName());
        }
        TableType tableType = tableTypeService.getById(bean.getTypeId());
        if(tableType != null){
            bean.setTypeName(tableType.getTypeName());
            bean.setMinNum(tableType.getMinNum());
            bean.setMaxNum(tableType.getMaxNum());
        }
        return this.save(bean);
    }

    @Override
    public boolean edit(TableParam param) {
        Table bean = new Table();
        BeanUtils.copyProperties(param,bean);
        TableArea area = tableAreaService.getById(bean.getAreaId());
        if(area != null){
            bean.setAreaName(area.getAreaName());
        }
        TableType tableType = tableTypeService.getById(bean.getTypeId());
        if(tableType != null){
            bean.setTypeName(tableType.getTypeName());
            bean.setMinNum(tableType.getMinNum());
            bean.setMaxNum(tableType.getMaxNum());
        }
        return this.updateById(bean);
    }

    @Override
    public boolean delById(Integer tableId) {
        return this.removeById(tableId);
    }
}
