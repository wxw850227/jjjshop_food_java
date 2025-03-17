package net.jjjshop.shop.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopLoginLog;
import net.jjjshop.common.mapper.shop.ShopLoginLogMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.shop.LoginLogPageParam;
import net.jjjshop.shop.service.shop.ShopLoginLogService;
import net.jjjshop.shop.vo.shop.LoginLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 管理员登录记录表 服务实现类
 * @author jjjshop
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopLoginLogServiceImpl extends BaseServiceImpl<ShopLoginLogMapper, ShopLoginLog> implements ShopLoginLogService {

    /**
     * 获取所有登陆记录
     * @param loginLogPageParam
     * @return
     */
    public Paging<LoginLogVo> getList(LoginLogPageParam loginLogPageParam){
        Page<ShopLoginLog> page = new PageInfo(loginLogPageParam);
        LambdaQueryWrapper<ShopLoginLog> wrapper = new LambdaQueryWrapper<ShopLoginLog>();
        if(StringUtils.isNotEmpty(loginLogPageParam.getUsername())){
            wrapper.like(ShopLoginLog::getUsername, loginLogPageParam.getUsername());
        }
        wrapper.orderByDesc(ShopLoginLog::getCreateTime);
        IPage<ShopLoginLog> iPage = this.page(page, wrapper);
        IPage<LoginLogVo> result = iPage.convert(item -> {
            LoginLogVo vo = new LoginLogVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        });
        return new Paging(result);
    }


}
