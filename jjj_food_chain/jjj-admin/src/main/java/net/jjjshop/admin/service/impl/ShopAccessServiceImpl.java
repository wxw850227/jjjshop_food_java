package net.jjjshop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.param.ShopAccessParam;
import net.jjjshop.admin.service.ShopAccessService;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.mapper.shop.ShopAccessMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商家用户权限表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
@Slf4j
@Service
public class ShopAccessServiceImpl extends BaseServiceImpl<ShopAccessMapper, ShopAccess> implements ShopAccessService {
    @Autowired
    private ShopAccessMapper shopAccessMapper;

    public List<ShopAccess> getAllPlus(){
        // 查找插件根节点
        LambdaQueryWrapper<ShopAccess> plusWrapper = Wrappers.lambdaQuery();
        plusWrapper.eq(ShopAccess::getPath, "/plus/plus/index");
        ShopAccess plusBean = this.getOne(plusWrapper);
        // 查找插件
        LambdaQueryWrapper<ShopAccess> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ShopAccess::getParentId, plusBean.getAccessId());
        wrapper.eq(ShopAccess::getPlusCategoryId, 0);
        // 获取所有已启用的权限列表
        return this.list(wrapper);
    }


    public Boolean clearPlusCategoryId(Integer accessId){
        ShopAccess access = new ShopAccess();
        access.setAccessId(accessId);
        access.setPlusCategoryId(0);
        return this.updateById(access);
    }

    public Boolean editPlusCategoryId(Integer accessId, Integer plusCategoryId){
        ShopAccess access = new ShopAccess();
        access.setAccessId(accessId);
        access.setPlusCategoryId(plusCategoryId);
        return this.updateById(access);
    }

    public Boolean editStatusById(Integer accessId, Integer status){
        ShopAccess access = new ShopAccess();
        access.setAccessId(accessId);
        access.setIsShow(status);
        return this.updateById(access);
    }

    /**
     * 真删除
     * @param accessId
     * @return
     */
    public Boolean delById(Integer accessId){
        // 是否存在子菜单
        LambdaQueryWrapper<ShopAccess> countWrapper = Wrappers.lambdaQuery();
        countWrapper.eq(ShopAccess::getParentId, accessId);
        if(this.count(countWrapper) > 0){
            throw new BusinessException("当前菜单下存在子权限，请先删除");
        }
        return this.removeById(accessId);
    }

    /**
     * 新增
     * @param shopAccessParam
     * @return
     */
    public Boolean add(ShopAccessParam shopAccessParam){
        LambdaQueryWrapper<ShopAccess> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopAccess::getPath,shopAccessParam.getPath());
        if(this.count(wrapper) > 0){
            throw new BusinessException("路径已存在，请更改后再试");
        }
        ShopAccess access = new ShopAccess();
        BeanUtils.copyProperties(shopAccessParam, access);
        // 11位时间戳
        String timestamp = String.valueOf((new Date()).getTime()/1000);
        access.setAccessId(Integer.valueOf(timestamp));
        return this.save(access);
    }

    /**
     * 修改
     * @param shopAccessParam
     * @return
     */
    public Boolean edit(ShopAccessParam shopAccessParam){
        ShopAccess access = new ShopAccess();
        BeanUtils.copyProperties(shopAccessParam, access);
        return this.updateById(access);
    }

    /**
     * 更改门店菜单状态
     */
    @Override
    public Boolean editSupplierStatusById(Integer id, Integer status) {
        ShopAccess access = new ShopAccess();
        access.setAccessId(id);
        //是否门店菜单0否1是
        access.setIsSupplier(status);
        return this.updateById(access);
    }
}
