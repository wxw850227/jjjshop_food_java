

package net.jjjshop.framework.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.entity.SysLoginLog;
import net.jjjshop.framework.log.mapper.SysLoginLogMapper;
import net.jjjshop.framework.log.param.SysLoginLogPageParam;
import net.jjjshop.framework.log.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统登录日志 服务实现类
 */
@Slf4j
@Service
public class SysLoginLogServiceImpl extends BaseServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysLoginLog(SysLoginLog sysLoginLog) throws Exception {
        return super.save(sysLoginLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysLoginLog(SysLoginLog sysLoginLog) throws Exception {
        return super.updateById(sysLoginLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysLoginLog(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<SysLoginLog> getSysLoginLogPageList(SysLoginLogPageParam sysLoginLogPageParam) throws Exception {
        Page<SysLoginLog> page = new PageInfo<>(sysLoginLogPageParam,OrderItem.desc(getLambdaColumn(SysLoginLog::getCreateTime)));
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        IPage<SysLoginLog> iPage = sysLoginLogMapper.selectPage(page, wrapper);
        return new Paging<SysLoginLog>(iPage);
    }

}
