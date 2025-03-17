package net.jjjshop.common.service.app.impl;

import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.mapper.app.AppWxMapper;
import net.jjjshop.common.service.app.AppWxService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信小程序记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-03
 */
@Slf4j
@Service
public class AppWxServiceImpl extends BaseServiceImpl<AppWxMapper, AppWx> implements AppWxService {

    @Autowired
    private AppWxMapper appWxMapper;

}
