package net.jjjshop.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.param.AppPageParam;
import net.jjjshop.admin.param.AppParam;
import net.jjjshop.admin.service.AppService;
import net.jjjshop.admin.service.ShopUserService;
import net.jjjshop.admin.vo.AppVo;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.entity.shop.ShopUser;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.mapper.app.AppMapper;
import net.jjjshop.common.service.product.ProductCategoryService;
import net.jjjshop.common.service.page.PageService;
import net.jjjshop.common.service.supplier.SupplierService;
import net.jjjshop.common.util.PageUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信小程序记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-23
 */
@Slf4j
@Service
public class AppServiceImpl extends BaseServiceImpl<AppMapper, App> implements AppService {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private PageService pageService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ProductCategoryService productCategoryService;
    /**
     * app列表
     * @param appPageParam
     * @return
     */
    public Paging<AppVo> getList(AppPageParam appPageParam){
        Page<App> page = new PageInfo<>(appPageParam, OrderItem.asc(getLambdaColumn(App::getCreateTime)));
        IPage<App> iPage = this.page(page, new LambdaUpdateWrapper<App>().eq(App::getIsDelete, false));
        // 最终返回分页对象
        IPage<AppVo> resultPage = iPage.convert(result -> {
            return transVo(result);
        });
        return new Paging(resultPage);
    }

    public AppVo transVo(App result){
        AppVo vo = new AppVo();
        BeanUtil.copyProperties(result, vo);
        ShopUser shopUser = shopUserService.getOne(new LambdaQueryWrapper<ShopUser>()
                .eq(ShopUser::getAppId, vo.getAppId()).eq(ShopUser::getIsSuper, true));
        if(shopUser != null){
            vo.setUserName(shopUser.getUserName());
        }
        return vo;
    }

    /**
     * 修改状态
     * @param appId
     * @return
     */
    public Boolean editStatusById(Integer appId){
        App app = this.getById(appId);
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setIsRecycle(!app.getIsRecycle());
        return this.updateById(updateBean);
    }

    /**
     * 修改微信服务商支付状态
     * @param appId
     * @return
     */
    public Boolean updateWxStatus(Integer appId){
        App app = this.getById(appId);
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setWeixinService(app.getWeixinService()?false:true);
        return this.updateById(updateBean);
    }

    /**
     * 删除
     * @param appId
     * @return
     */
    public Boolean setDelete(Integer appId){
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setIsDelete(true);
        return this.updateById(updateBean);
    }

    /**
     * 新增
     * @param appParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(AppParam appParam){
        // 是否存在用户名
        if(shopUserService.count(new LambdaQueryWrapper<ShopUser>()
                .eq(ShopUser::getUserName, appParam.getUserName())) > 0){
            throw new BusinessException("商家用户名已存在");
        }
        // 保存主表
        App app = new App();
        BeanUtils.copyProperties(appParam, app);
        // 不过期
        if(appParam.getNoExpire()){
            app.setExpireTime(null);
        }else{
            app.setExpireTime(DateUtil.parseDateTime(appParam.getExpireTime() + " 23:59:59"));
        }
        this.save(app);
        if(app.getExpireTime() == null){
            this.updateById(app);
        }

        //新增门店
        Supplier supplier = new Supplier();
        supplier.setAppId(app.getAppId());
        //供应商姓名
        supplier.setName(appParam.getUserName());
        //真实姓名
        supplier.setRealName(appParam.getUserName());
        //是否总店，0否1是
        supplier.setIsMain(1);
        //外卖配送方式
        supplier.setDeliverySet("10,20");
        //店内用餐方式
        supplier.setStoreSet("30,40");
        //外卖营业时间
        supplier.setDeliveryTime("00:00,23:59");
        //自提营业时间
        supplier.setPickTime("00:00,23:59");
        supplier.setStoreTime("00:00,23:59");
        supplierService.save(supplier);

        //新增特殊分类
        String[] names = new String[]{"新品","热卖","套餐","新品","热卖","套餐"};
        Integer[] types = new Integer[]{0,0,0,1,1,1};
        List<ProductCategory> productCategoryList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName(names[i]);
            productCategory.setIsSpecial(true);
            productCategory.setType(types[i]);
            productCategory.setAppId(app.getAppId());
            productCategory.setShopSupplierId(supplier.getShopSupplierId());
            productCategoryList.add(productCategory);
        }
        productCategoryService.saveBatch(productCategoryList);

        // 保存管理员用户表
        ShopUser user = new ShopUser();
        //总店id
        user.setShopSupplierId(supplier.getShopSupplierId());
        user.setAppId(app.getAppId());
        user.setUserName(appParam.getUserName());
        user.setIsSuper(true);
        String salt = SaltUtil.generateSalt();
        // 密码加密
        user.setSalt(salt);
        user.setPassword(PasswordUtil.encrypt(appParam.getPassword(), salt));
        // 新增应用diy配置
        this.insertPageDefault(app.getAppId());
        return shopUserService.save(user);
    }

    /**
     * 页面默认数据
     */
    private void insertPageDefault(Integer appId){
        net.jjjshop.common.entity.page.Page page = PageUtils.getPage(10);
        page.setAppId(appId);
        pageService.save(page);
    }

    /**
     * 修改
     * @param appParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(AppParam appParam){
        // 更新主表
        App app = new App();
        app.setAppId(appParam.getAppId());
        app.setAppName(appParam.getAppName());
        // 不过期
        if(appParam.getNoExpire()){
            app.setExpireTime(null);
        }else{
            app.setExpireTime(DateUtil.parseDateTime(appParam.getExpireTime() + " 23:59:59"));
        }
        this.updateById(app);
        // 更新管理员表
        ShopUser user = shopUserService.getOne(new LambdaQueryWrapper<ShopUser>()
                .eq(ShopUser::getAppId, app.getAppId())
                .eq(ShopUser::getIsSuper, true));
        ShopUser updateUser = new ShopUser();
        if (!StringUtils.isEmpty(appParam.getPassword())) {
            updateUser.setPassword(PasswordUtil.encrypt(appParam.getPassword(), user.getSalt()));
        }
        if(!appParam.getUserName().equals(user.getUserName())){
            if(shopUserService.count(new LambdaQueryWrapper<ShopUser>()
                    .eq(ShopUser::getUserName, appParam.getUserName())) > 0){
                throw new BusinessException("商家用户名已存在");
            }
        }
        updateUser.setShopUserId(user.getShopUserId());
        updateUser.setUserName(appParam.getUserName());
        return shopUserService.updateById(updateUser);
    }

    /*
     *启用关闭连锁
     */
    @Override
    public boolean updateChain(Integer appId) {
        App app = this.getById(appId);
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setIsChain(!app.getIsChain());
        return this.updateById(updateBean);
    }
}
