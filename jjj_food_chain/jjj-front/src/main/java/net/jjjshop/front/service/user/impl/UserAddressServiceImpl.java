package net.jjjshop.front.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.supplier.Supplier;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserAddress;
import net.jjjshop.common.mapper.user.UserAddressMapper;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.vo.user.UserAddressVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.param.user.UserAddressParam;
import net.jjjshop.front.service.supplier.SupplierService;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户收货地址表 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */

@Slf4j
@Service
public class UserAddressServiceImpl extends BaseServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private UserService userService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private OrderUtils orderUtils;


    /**
     * 收货地址列表
     * @param userId
     * @return
     */
    public Map<String, Object> getList(Integer userId,Integer shopSupplierId) {
        Supplier supplier = null;
        if(shopSupplierId != null && shopSupplierId != 0){
            supplier = supplierService.getById(shopSupplierId);
        }
        HashMap<String, Object> map = new HashMap<>();
        User user = userService.getById(userId);
        Integer addressId = user.getAddressId();
        List<UserAddress> userAddresses = this.list(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId));
        Supplier finalSupplier = supplier;
        List<UserAddressVo> userAddressVos = userAddresses.stream().map(e -> {
            UserAddressVo vo = new UserAddressVo();
            BeanUtils.copyProperties(e, vo);
            if(finalSupplier != null){
                vo.setStatus(1);
                vo.setDistance(BigDecimal.valueOf(orderUtils.getDistance(finalSupplier, vo.getLongitude(), vo.getLatitude()))
                        .divide(new BigDecimal(1000),2, RoundingMode.DOWN));
                //大于配送范围km
                if(vo.getDistance().compareTo(BigDecimal.valueOf(finalSupplier.getDeliveryDistance())) > 0){
                    //0超出配送距离,1正常
                    vo.setStatus(0);
                }
            }
            return vo;
        }).collect(Collectors.toList());
        map.put("defaultId", addressId);
        map.put("list", userAddressVos);
        return map;
    }

    /**
     * 获取收货地区详情
     * @param vo
     * @return
     */
    private JSONObject getDetailRegion(UserAddressVo vo) {
        JSONObject detailRegion = new JSONObject();
        detailRegion.put("province", regionService.getById(vo.getProvinceId()).getName());
        detailRegion.put("city", regionService.getById(vo.getCityId()).getName());
        detailRegion.put("region", regionService.getById(vo.getRegionId()).getName());
        return detailRegion;
    }

    /**
     * 添加收货地址
     * @param userAddressParam
     * @return
     */
    public Boolean add(User user, UserAddressParam userAddressParam) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressParam, userAddress);
        userAddress.setUserId(user.getUserId());
        boolean save = this.save(userAddress);
        if (user.getAddressId() == null || user.getAddressId() == 0) {
            List<UserAddress> list = this.list(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, user.getUserId()));
            Integer addressId = list.get(list.size() - 1).getAddressId();
            this.defaultAddress(addressId, user);
        }
        return save;
    }

    /**
     * 设置为默认收货地址
     * @param addressId
     * @param user
     * @return
     */
    public Boolean defaultAddress(Integer addressId, User user) {
        return userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId()).set(User::getAddressId, addressId));
    }

    /**
     * 获取收货地址详情
     * @param addressId
     * @return
     */
    public UserAddressVo detail(Integer addressId) {
        UserAddress userAddress = this.getById(addressId);
        if(userAddress == null){
            return null;
        }
        UserAddressVo vo = new UserAddressVo();
        BeanUtils.copyProperties(userAddress, vo);
        return vo;
    }

    /**
     * 编辑地址
     * @param userAddressParam
     * @return
     */
    public Boolean edit(UserAddressParam userAddressParam) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressParam, userAddress);
        return this.updateById(userAddress);
    }

    /**
     * 真删除收货地址
     * @param addressId
     * @param user
     * @return
     */
    public Boolean delById(Integer addressId, User user) {
        //如果删除的是默认地址,则重置
        if(Objects.equals(user.getAddressId(), addressId)){
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId()).set(User::getAddressId, 0));
        }
        return this.removeById(this.getById(addressId));
    }

    @Override
    public Integer getaddressId() {

        return null;
    }

}
