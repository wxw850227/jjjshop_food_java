package net.jjjshop.shop.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductFeed;
import net.jjjshop.common.mapper.product.ProductFeedMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.common.param.product.FeedParam;
import net.jjjshop.shop.service.product.ProductFeedService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 加料库 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-14
 */
@Slf4j
@Service
public class ProductFeedServiceImpl extends BaseServiceImpl<ProductFeedMapper, ProductFeed> implements ProductFeedService {

    @Autowired
    private ProductFeedMapper productFeedMapper;

    @Override
    public Paging<ProductFeed> getList(CommonPageParam param) {
        Page<ProductFeed> page = new PageInfo<>(param);
        LambdaQueryWrapper<ProductFeed> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFeed::getShopSupplierId,ShopLoginUtil.getShopSupplierId());
        wrapper.orderByAsc(ProductFeed::getSort);
        wrapper.orderByDesc(ProductFeed::getCreateTime);
        IPage<ProductFeed> iPage = this.page(page, wrapper);
        return new Paging(iPage);
    }

    @Override
    public boolean add(FeedParam param) {
        int count = this.count(new LambdaQueryWrapper<ProductFeed>()
                .eq(ProductFeed::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .eq(ProductFeed::getFeedName,param.getFeedName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        ProductFeed bean = new ProductFeed();
        BeanUtils.copyProperties(param,bean);
        bean.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        return this.save(bean);
    }

    @Override
    public boolean edit(FeedParam param) {
        int count = this.count(new LambdaQueryWrapper<ProductFeed>()
                .eq(ProductFeed::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .ne(ProductFeed::getFeedId, param.getFeedId())
                .eq(ProductFeed::getFeedName,param.getFeedName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        ProductFeed bean = new ProductFeed();
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
            throw new BusinessException("请选择加料");
        }
        return this.remove(new LambdaUpdateWrapper<ProductFeed>()
                .in(ProductFeed::getFeedId, Arrays.stream(feedIds.split(",")).map(Integer::valueOf).collect(Collectors.toList())));
    }
}
