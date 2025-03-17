

package net.jjjshop.framework.log.service;

import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.entity.SysOperationLog;
import net.jjjshop.framework.log.param.SysOperationLogPageParam;

/**
 * 系统操作日志 服务类
 */
public interface SysOperationLogService extends BaseService<SysOperationLog> {

    /**
     * 保存
     *
     * @param sysOperationLog
     * @return
     * @throws Exception
     */
    boolean saveSysOperationLog(SysOperationLog sysOperationLog) throws Exception;

    /**
     * 修改
     *
     * @param sysOperationLog
     * @return
     * @throws Exception
     */
    boolean updateSysOperationLog(SysOperationLog sysOperationLog) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysOperationLog(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param sysOperationLogQueryParam
     * @return
     * @throws Exception
     */
    Paging<SysOperationLog> getSysOperationLogPageList(SysOperationLogPageParam sysOperationLogPageParam) throws Exception;

}
