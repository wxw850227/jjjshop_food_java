package net.jjjshop.shop.service.settings;

import net.jjjshop.common.entity.settings.Printer;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.setting.PrinterPageParam;
import net.jjjshop.shop.param.setting.PrinterParam;

import java.util.List;

/**
 * 小票打印机记录表 服务类
 * @author jjjshop
 * @since 2022-07-20
 */
public interface PrinterService extends BaseService<Printer> {
    /**
     * 分页查询小票打印机
     * @param printerPageParam
     * @return
     */
    Paging<Printer> getList(PrinterPageParam printerPageParam);

    /**
     * 获取所有小票打印机
     * @param
     * @return
     */
    List<Printer> getAll();

    /**
     * 添加小票打印机
     * @param printerParam
     * @return
     */
    boolean add(PrinterParam printerParam);

    /**
     * 查询小票打印机种类
     * @param
     * @return
     */
    List<String> getPrinterTypeName();

    /**
     * 修改小票打印机
     * @param printerParam
     * @return
     */
    boolean edit(PrinterParam printerParam);

    /**
     * 删除小票打印机
     * @param id
     * @return
     */
    boolean setDelete(Integer id);
}
