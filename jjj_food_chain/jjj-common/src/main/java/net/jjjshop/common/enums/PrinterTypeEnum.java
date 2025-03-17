package net.jjjshop.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public enum PrinterTypeEnum{
    FEI_E_YUN("飞鹅打印机", "FEI_E_YUN", "FeiE"),
    PRINTER_CENTER("365云打印", "PRINT_CENTER", "PrintCenter");


    private String text;
    private String printerType;
    private String className;

    public static List<String> getTypeName(){
        List<String> result = new ArrayList<>();
        for (PrinterTypeEnum type : EnumSet.allOf(PrinterTypeEnum.class)){
            result.add(type.getText());
        }
        return result;
    }

    //查找payType
    public static String getClassName(String printerType) {
        String className = null;
        PrinterTypeEnum[] enums = values();    //获取所有枚举集合
        for (PrinterTypeEnum item : enums) {
            if(item.getPrinterType().equals(printerType)){
                className = item.getClassName();
                break;
            }
        }
        return className;
    }
}
