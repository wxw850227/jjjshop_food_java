package net.jjjshop.common.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.service.shop.ShopAccessService;
import net.jjjshop.common.vo.shop.ShopAccessVo;
import net.jjjshop.config.constant.CommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ShopAccessUtils {
    @Autowired
    private ShopAccessService shopAccessService;

    public List<ShopAccessVo> getAll(){
        LambdaQueryWrapper<ShopAccess> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(ShopAccess::getSort).orderByAsc(ShopAccess::getCreateTime);
        wrapper.comment(CommonConstant.NOT_WITH_App_Id);
        // 获取所有权限列表
        List<ShopAccess> list = shopAccessService.list(wrapper);
        // 转成vo
        List<ShopAccessVo> voList = list.stream().map(e -> {
            ShopAccessVo shopAccessVo = new ShopAccessVo();
            BeanUtils.copyProperties(e, shopAccessVo);
            return shopAccessVo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<ShopAccessVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    public List<ShopAccessVo> getShopAll(){
        LambdaQueryWrapper<ShopAccess> wrapper = Wrappers.lambdaQuery();
        //是否显示1=显示0=不显示
        wrapper.eq(ShopAccess::getIsShow,1);
        wrapper.orderByAsc(ShopAccess::getSort).orderByAsc(ShopAccess::getCreateTime);
        wrapper.comment(CommonConstant.NOT_WITH_App_Id);
        // 获取所有权限列表
        List<ShopAccess> list = shopAccessService.list(wrapper);
        // 转成vo
        List<ShopAccessVo> voList = list.stream().map(e -> {
            ShopAccessVo shopAccessVo = new ShopAccessVo();
            BeanUtils.copyProperties(e, shopAccessVo);
            return shopAccessVo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<ShopAccessVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<ShopAccessVo> getChildList(ShopAccess bean, List<ShopAccessVo> voList) {
        List<ShopAccessVo> list = voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getAccessId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
        if(list.size() == 0){
            return null;
        }
        return list;
    }
}
