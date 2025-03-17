package net.jjjshop.shop.service.supplier.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopUser;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.supplier.SupplierMapper;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.util.PasswordUtil;
import net.jjjshop.shop.param.supplier.SupplierPageParam;
import net.jjjshop.shop.param.supplier.SupplierParam;

import net.jjjshop.shop.param.supplier.SupplierSettingParam;
import net.jjjshop.shop.service.shop.ShopUserService;
import net.jjjshop.shop.service.supplier.SupplierService;
import net.jjjshop.shop.vo.supplier.SupplierVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商表 服务实现类
 *
 * @author jjjshop
 * @since 2022-10-17
 */
@Slf4j
@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private RegionService regionService;

    /**
     * 商户分页列表
     * @param supplierPageParam
     * @return
     */
    public Paging<SupplierVo> getList(SupplierPageParam supplierPageParam) {
        Page<Supplier> page = new PageInfo<>(supplierPageParam);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(supplierPageParam.getSearch())) {
            wrapper.like(Supplier::getName, supplierPageParam.getSearch());
        }
        wrapper.eq(Supplier::getIsDelete, 0);
        wrapper.orderByDesc(Supplier::getCreateTime);
        IPage<Supplier> iPage = this.page(page, wrapper);
        IPage<SupplierVo> result = iPage.convert(item -> {
            SupplierVo vo = new SupplierVo();
            BeanUtils.copyProperties(item, vo);
            vo.setBusinessFilePath(uploadFileUtils.getFilePath(vo.getBusinessId()));
            ShopUser user = shopUserService.getOne(new LambdaQueryWrapper<ShopUser>()
                    .eq(ShopUser::getIsSuper, 1)
                    .eq(ShopUser::getShopSupplierId, vo.getShopSupplierId()));
            vo.setUserName(user!=null?user.getUserName():"");
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 获取编辑参数
     * @param id
     * @return
     */
    public SupplierVo toEdit(Integer id) {
        Supplier supplier = this.getById(id);
        SupplierVo vo = new SupplierVo();
        BeanUtils.copyProperties(supplier, vo);
        vo.setBusinessFilePath(uploadFileUtils.getFilePath(vo.getBusinessId()));
        ShopUser user = shopUserService.getOne(new LambdaQueryWrapper<ShopUser>()
                .eq(ShopUser::getIsSuper, 1)
                .eq(ShopUser::getShopSupplierId, vo.getShopSupplierId()));
        vo.setUserName(user!=null?user.getUserName():"");
        if (StringUtils.isNotEmpty(supplier.getLatitude())) {
            vo.setCoordinate(supplier.getLatitude() + "," + supplier.getLongitude());
        }
        return vo;
    }

    /**
     * 编辑供应商
     * @param supplierParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(SupplierParam supplierParam) {
        ShopUser user = shopUserService.getOne(new LambdaQueryWrapper<ShopUser>()
                .eq(ShopUser::getIsSuper, 1)
                .eq(ShopUser::getShopSupplierId, supplierParam.getShopSupplierId()));
        //修改供应商
        if (StringUtils.isNotEmpty(supplierParam.getCoordinate())) {
            String[] split = supplierParam.getCoordinate().split(",");
            if (split.length < 2) {
                supplierParam.setLatitude(split[0]);
            } else {
                supplierParam.setLatitude(split[0]);
                supplierParam.setLongitude(split[1]);
            }
        }
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierParam, supplier);
        this.updateById(supplier);
        // 修改登录用户
        if(user != null && StringUtils.isNotEmpty(supplierParam.getPassword()) && StringUtils.isNotEmpty(supplierParam.getConfirmPassword())){
            if (supplierParam.getPassword().equals(supplierParam.getConfirmPassword())) {
                // 盐值
                String salt = SaltUtil.generateSalt();
                user.setSalt(salt);
                // 密码加密
                user.setPassword(PasswordUtil.encrypt(supplierParam.getPassword(), salt));
            }else {
                throw new BusinessException("两次密码不一致");
            }
            shopUserService.updateById(user);
        }
        return true;
    }

    @Override
    public SupplierVo editDetail(Integer shopSupplierId) {
        Supplier supplier = this.getById(shopSupplierId);
        SupplierVo vo = new SupplierVo();
        BeanUtils.copyProperties(supplier, vo);
        //店内营业时间
        vo.setStoreTimeList(Arrays.stream(vo.getStoreTime().split(",")).collect(Collectors.toList()));
        //自提营业时间
        vo.setPickTimeList(Arrays.stream(vo.getPickTime().split(",")).collect(Collectors.toList()));
        //外卖营业时间
        vo.setDeliveryTimeList(Arrays.stream(vo.getDeliveryTime().split(",")).collect(Collectors.toList()));
        //外卖配送方式
        vo.setDeliverySetList(Arrays.stream(vo.getDeliverySet().split(",")).map(Integer::valueOf).collect(Collectors.toList()));
        //店内用餐方式
        vo.setStoreSetList(Arrays.stream(vo.getStoreSet().split(",")).map(Integer::valueOf).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public boolean setting(SupplierSettingParam param) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(param,supplier);
        supplier.setStoreTime(String.join(",", param.getStoreTimeList()));
        supplier.setPickTime(String.join(",", param.getPickTimeList()));
        supplier.setDeliveryTime(String.join(",", param.getDeliveryTimeList()));
        supplier.setDeliverySet(Arrays.stream(param.getDeliverySetList()).map(Object::toString).collect(Collectors.joining(",")));
        supplier.setStoreSet(Arrays.stream(param.getStoreSetList()).map(Object::toString).collect(Collectors.joining(",")));
        return this.updateById(supplier);
    }

    @Override
    public Integer getSupplierTotalByDay(String date) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getIsDelete,0);
        if(StringUtils.isNotEmpty(date)){
            wrapper.ge(Supplier::getCreateTime,  date + " 00:00:00");
            wrapper.le(Supplier::getCreateTime, date + " 23:59:59");
        }
        return this.count(wrapper);
    }
}
