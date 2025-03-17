package net.jjjshop.shop.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductUnit;
import net.jjjshop.common.mapper.product.ProductUnitMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.product.UnitParam;
import net.jjjshop.shop.service.product.ProductUnitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 单位库 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class ProductUnitServiceImpl extends BaseServiceImpl<ProductUnitMapper, ProductUnit> implements ProductUnitService {

    @Autowired
    private ProductUnitMapper productUnitMapper;

    @Override
    public Paging<ProductUnit> getList(CommonPageParam param) {
        Page<ProductUnit> page = new PageInfo<>(param);
        LambdaQueryWrapper<ProductUnit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductUnit::getShopSupplierId,ShopLoginUtil.getShopSupplierId());
        wrapper.orderByAsc(ProductUnit::getSort);
        wrapper.orderByDesc(ProductUnit::getCreateTime);
        IPage<ProductUnit> iPage = this.page(page, wrapper);
        return new Paging(iPage);
    }

    @Override
    public boolean add(UnitParam param) {
        int count = this.count(new LambdaQueryWrapper<ProductUnit>()
                .eq(ProductUnit::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .eq(ProductUnit::getUnitName,param.getUnitName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        ProductUnit bean = new ProductUnit();
        BeanUtils.copyProperties(param,bean);
        bean.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        return this.save(bean);
    }

    @Override
    public boolean edit(UnitParam param) {
        int count = this.count(new LambdaQueryWrapper<ProductUnit>()
                .eq(ProductUnit::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .ne(ProductUnit::getUnitId, param.getUnitId())
                .eq(ProductUnit::getUnitName,param.getUnitName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        ProductUnit bean = new ProductUnit();
        BeanUtils.copyProperties(param,bean);
        return this.updateById(bean);
    }

    @Override
    public boolean delById(Integer feedId) {
        return this.removeById(feedId);
    }

    //批量删除
    @Override
    public boolean deletes(String feedIds) {
        if(StringUtils.isEmpty(feedIds)){
            throw new BusinessException("请选择单位");
        }
        return this.remove(new LambdaUpdateWrapper<ProductUnit>()
                .in(ProductUnit::getUnitId, Arrays.stream(feedIds.split(",")).map(Integer::valueOf).collect(Collectors.toList())));
    }

}
