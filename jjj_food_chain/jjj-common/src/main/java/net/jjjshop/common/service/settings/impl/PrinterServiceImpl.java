package net.jjjshop.common.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Printer;
import net.jjjshop.common.mapper.settings.PrinterMapper;
import net.jjjshop.common.service.settings.PrinterService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 小票打印机记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-20
 */
@Slf4j
@Service
public class PrinterServiceImpl extends BaseServiceImpl<PrinterMapper, Printer> implements PrinterService {

}
