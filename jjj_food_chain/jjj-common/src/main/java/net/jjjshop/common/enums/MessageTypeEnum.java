package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
    MASTER("订单", 10),
    POINTS("分销", 20);

    private String name;
    private Integer value;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        MessageTypeEnum[] enums = values();    //获取所有枚举集合
        for (MessageTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
