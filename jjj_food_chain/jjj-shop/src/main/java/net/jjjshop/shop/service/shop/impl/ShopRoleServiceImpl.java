package net.jjjshop.shop.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopRole;
import net.jjjshop.common.entity.shop.ShopRoleAccess;
import net.jjjshop.common.mapper.shop.ShopRoleMapper;
import net.jjjshop.common.mapper.shop.ShopUserRoleMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.param.shopUser.ShopRoleParam;
import net.jjjshop.shop.service.shop.ShopRoleAccessService;
import net.jjjshop.shop.service.shop.ShopRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家用户角色表 服务实现类
 * @author jjjshop
 * @since 2022-07-09
 */
@Slf4j
@Service
public class ShopRoleServiceImpl extends BaseServiceImpl<ShopRoleMapper, ShopRole> implements ShopRoleService {
    @Autowired
    private ShopRoleAccessService shopRoleAccessService;
    @Autowired
    private ShopUserRoleMapper shopUserRoleMapper;

    /**
     * 角色列表
     * @param
     * @return
     */
    public List<ShopRole> getList(){
        return this.list(new LambdaQueryWrapper<ShopRole>().orderByAsc(ShopRole::getSort)
                .orderByAsc(ShopRole::getCreateTime));
    }
    /**
     * 新增角色
     * @param shopRoleParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(ShopRoleParam shopRoleParam){
        ShopRole role = new ShopRole();
        BeanUtils.copyProperties(shopRoleParam, role);
        // 保存主表
        this.save(role);
        // 保存权限
        this.saveAccess(shopRoleParam, role.getRoleId());
        return true;
    }
    /**
     * 获取已选择权限
     * @param roleId
     * @return
     */
    public List<Integer> getSelectList(Integer roleId){
        List<ShopRoleAccess> accessList = shopRoleAccessService.list(new LambdaQueryWrapper<ShopRoleAccess>()
                        .eq(ShopRoleAccess::getRoleId, roleId));
        List<Integer> idList = new ArrayList<>();
        accessList.forEach(item ->{
            idList.add(item.getAccessId());
        });
        return idList;
    }

    /**
     * 编辑角色
     * @param shopRoleParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(ShopRoleParam shopRoleParam){
        ShopRole role = new ShopRole();
        BeanUtils.copyProperties(shopRoleParam, role);
        // 保存主表
        this.updateById(role);
        // 先删除权限
        shopRoleAccessService.remove(new LambdaQueryWrapper<ShopRoleAccess>()
                .eq(ShopRoleAccess::getRoleId, shopRoleParam.getRoleId()));
        // 保存权限
        this.saveAccess(shopRoleParam, role.getRoleId());
        return true;
    }

    /**
     * 保存权限
     * @param shopRoleParam
     * @param roleId
     */
    private void saveAccess(ShopRoleParam shopRoleParam, Integer roleId){
        List<ShopRoleAccess> accessList = new ArrayList<>();
        shopRoleParam.getAccessId().forEach(item -> {
            ShopRoleAccess access = new ShopRoleAccess();
            access.setRoleId(roleId);
            access.setAccessId(item);
            accessList.add(access);
        });
        shopRoleAccessService.saveBatch(accessList);
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer roleId){
        // 判断是否有用户引用该角色
        Integer count = shopUserRoleMapper.getUserRoleCount(roleId);
        if(count > 0){
            throw new BusinessException("当前角色下存在用户，不允许删除");
        }
        shopRoleAccessService.remove(new LambdaQueryWrapper<ShopRoleAccess>().eq(ShopRoleAccess::getRoleId, roleId));
        return this.removeById(roleId);
    }
}
