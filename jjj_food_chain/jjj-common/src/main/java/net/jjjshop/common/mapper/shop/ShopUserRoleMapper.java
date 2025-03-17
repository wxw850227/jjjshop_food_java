package net.jjjshop.common.mapper.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.shop.ShopUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 商家用户角色记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-09
 */
@Repository
public interface ShopUserRoleMapper extends BaseMapper<ShopUserRole> {
    /**
     * 根据角色id查询下面的用户数量
     * @param roleId
     * @return
     */
    Integer getUserRoleCount(Integer roleId);

    /**
     * 通过用户查找角色名称
     * @param shopUserId
     * @return
     */
    List<Map> getRoleListByUser(Integer shopUserId);

    List<Integer> getRoleIdListByUser(Integer shopUserId);
}
