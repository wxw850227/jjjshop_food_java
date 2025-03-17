package net.jjjshop.common.factory.printer;

import net.jjjshop.common.entity.settings.Printer;

/**
 * 打印接口类
 */
public abstract class PrinterFactoryService {
    public abstract void printTicket(Printer printer, String content);
}
