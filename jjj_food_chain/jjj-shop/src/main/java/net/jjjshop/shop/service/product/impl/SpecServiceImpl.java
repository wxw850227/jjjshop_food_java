package net.jjjshop.shop.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductUnit;
import net.jjjshop.common.entity.product.Spec;
import net.jjjshop.common.mapper.product.SpecMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.product.SpecParam;
import net.jjjshop.shop.service.product.SpecService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 商品规格组记录表 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-18
 */
@Slf4j
@Service
public class SpecServiceImpl extends BaseServiceImpl<SpecMapper, Spec> implements SpecService {

    @Autowired
    private SpecMapper specMapper;

    @Override
    public Paging<Spec> getList(CommonPageParam param) {
        Page<Spec> page = new PageInfo<>(param);
        LambdaQueryWrapper<Spec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spec::getShopSupplierId, ShopLoginUtil.getShopSupplierId());
        wrapper.orderByAsc(Spec::getSort);
        wrapper.orderByDesc(Spec::getCreateTime);
        IPage<Spec> iPage = this.page(page, wrapper);
        return new Paging(iPage);
    }

    @Override
    public boolean add(SpecParam param) {
        int count = this.count(new LambdaQueryWrapper<Spec>()
                .eq(Spec::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .eq(Spec::getSpecName,param.getSpecName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        Spec bean = new Spec();
        BeanUtils.copyProperties(param,bean);
        bean.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        return this.save(bean);
    }

    @Override
    public boolean edit(SpecParam param) {
        int count = this.count(new LambdaQueryWrapper<Spec>()
                .eq(Spec::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .ne(Spec::getSpecId, param.getSpecId())
                .eq(Spec::getSpecName,param.getSpecName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        Spec bean = new Spec();
        BeanUtils.copyProperties(param,bean);
        return this.updateById(bean);
    }

    @Override
    public boolean delById(Integer specId) {
        return this.removeById(specId);
    }

    @Override
    public boolean deletes(String specIds) {
        if(StringUtils.isEmpty(specIds)){
            throw new BusinessException("请选择规格");
        }
        return this.remove(new LambdaUpdateWrapper<Spec>()
                .in(Spec::getSpecId, Arrays.stream(specIds.split(",")).map(Integer::valueOf).collect(Collectors.toList())));
    }
}
