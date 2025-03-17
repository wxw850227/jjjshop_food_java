

package net.jjjshop.framework.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.entity.SysOperationLog;
import net.jjjshop.framework.log.mapper.SysOperationLogMapper;
import net.jjjshop.framework.log.param.SysOperationLogPageParam;
import net.jjjshop.framework.log.service.SysOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统操作日志 服务实现类
 */
@Slf4j
@Service
public class SysOperationLogServiceImpl extends BaseServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysOperationLog(SysOperationLog sysOperationLog) throws Exception {
        return super.save(sysOperationLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysOperationLog(SysOperationLog sysOperationLog) throws Exception {
        return super.updateById(sysOperationLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysOperationLog(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<SysOperationLog> getSysOperationLogPageList(SysOperationLogPageParam sysOperationLogPageParam) throws Exception {
        Page<SysOperationLog> page = new PageInfo<>(sysOperationLogPageParam,OrderItem.desc(getLambdaColumn(SysOperationLog::getCreateTime)));
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        IPage<SysOperationLog> iPage = sysOperationLogMapper.selectPage(page, wrapper);
        return new Paging<SysOperationLog>(iPage);
    }

}
