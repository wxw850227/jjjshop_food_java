package net.jjjshop.shop.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductAttribute;
import net.jjjshop.common.mapper.product.ProductAttributeMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.common.param.product.AttrParam;
import net.jjjshop.shop.service.product.ProductAttributeService;
import net.jjjshop.common.vo.product.ProductAttrVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 属性库 服务实现类
 *
 * @author jjjfood
 * @since 2023-12-18
 */
@Slf4j
@Service
public class ProductAttributeServiceImpl extends BaseServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Override
    public Paging<ProductAttrVo> getList(BasePageOrderParam param) {
        Page<ProductAttribute> page = new PageInfo<>(param);
        LambdaQueryWrapper<ProductAttribute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductAttribute::getShopSupplierId,ShopLoginUtil.getShopSupplierId());
        wrapper.orderByAsc(ProductAttribute::getSort);
        wrapper.orderByDesc(ProductAttribute::getCreateTime);
        IPage<ProductAttribute> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductAttrVo> resultPage = iPage.convert(result -> {
            return transVo(result);
        });
        return new Paging(iPage);
    }

    public ProductAttrVo transVo(ProductAttribute bean){
        ProductAttrVo vo = new ProductAttrVo();
        BeanUtil.copyProperties(bean, vo);
        if(StringUtils.isNotEmpty(bean.getAttributeValue())){
            //字符串转数组
            vo.setAttributeValueList(Arrays.stream(bean.getAttributeValue().split("\\|")).collect(Collectors.toList()));
        }
        return vo;
    }

    @Override
    public boolean add(AttrParam param) {
        if(CollectionUtils.isEmpty(param.getAttributeValue())){
            throw new BusinessException("属性值不能为空");
        }
        int count = this.count(new LambdaQueryWrapper<ProductAttribute>()
                .eq(ProductAttribute::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .eq(ProductAttribute::getAttributeName,param.getAttributeName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        ProductAttribute bean = new ProductAttribute();
        BeanUtils.copyProperties(param,bean);
        bean.setShopSupplierId(ShopLoginUtil.getShopSupplierId());
        //数组转字符串
        bean.setAttributeValue(String.join("|", param.getAttributeValue()));
        return this.save(bean);
    }

    @Override
    public boolean edit(AttrParam param) {
        if(CollectionUtils.isEmpty(param.getAttributeValueList())){
            throw new BusinessException("属性值不能为空");
        }
        int count = this.count(new LambdaQueryWrapper<ProductAttribute>()
                .eq(ProductAttribute::getShopSupplierId, ShopLoginUtil.getShopSupplierId())
                .ne(ProductAttribute::getAttributeId, param.getAttributeId())
                .eq(ProductAttribute::getAttributeName,param.getAttributeName()));
        if(count > 0){
            throw new BusinessException("名称已存在");
        }
        ProductAttribute bean = new ProductAttribute();
        BeanUtils.copyProperties(param,bean);
        //数组转字符串
        bean.setAttributeValue(String.join("|", param.getAttributeValueList()));
        return this.updateById(bean);
    }

    @Override
    public boolean delById(Integer attributeId) {
        return this.removeById(attributeId);
    }

    //批量删除
    @Override
    public boolean deletes(String attributeIds) {
        if(StringUtils.isEmpty(attributeIds)){
            throw new BusinessException("请选择属性");
        }
        return this.remove(new LambdaUpdateWrapper<ProductAttribute>()
                .in(ProductAttribute::getAttributeId, Arrays.stream(attributeIds.split(",")).map(Integer::valueOf).collect(Collectors.toList())));
    }
}
