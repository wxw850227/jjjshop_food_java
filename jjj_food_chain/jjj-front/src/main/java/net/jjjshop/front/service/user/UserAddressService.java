package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserAddress;
import net.jjjshop.common.vo.user.UserAddressVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.param.user.UserAddressParam;

import java.util.Map;

/**
 * 用户收货地址表 服务类
 * @author jjjshop
 * @since 2022-08-02
 */

public interface UserAddressService extends BaseService<UserAddress> {

    /**
     * 收货地址列表
     * @param userId
     * @return
     */
    Map<String, Object> getList(Integer userId,Integer shopSupplierId);

    /**
     * 添加收货地址
     * @param userAddressParam
     * @return
     */
    Boolean add(User user, UserAddressParam userAddressParam);

    /**
     * 获取收货地址详情
     * @param addressId
     * @return
     */
    UserAddressVo detail(Integer addressId);

    /**
     * 编辑地址
     * @param userAddressParam
     * @return
     */
    Boolean edit(UserAddressParam userAddressParam);

    /**
     * 设置为默认收货地址
     * @param addressId
     * @param user
     * @return
     */
    Boolean defaultAddress(Integer addressId,User user);

    /**
     * 真删除收货地址
     * @param addressId
     * @param user
     * @return
     */
    Boolean delById(Integer addressId, User user);

    Integer getaddressId();
}
