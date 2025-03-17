package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 */
@Getter
@AllArgsConstructor
public enum OrderSourceEnum {
    MASTER("普通", 10, "Master"),
    POINTS("积分", 20,"Points");

    private String name;
    private Integer value;
    private String eName;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        OrderSourceEnum[] enums = values();    //获取所有枚举集合
        for (OrderSourceEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }

    //查找eName
    public static String getEName(Integer value) {
        String eName = null;
        OrderSourceEnum[] enums = values();    //获取所有枚举集合
        for (OrderSourceEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                eName = item.getEName();
                break;
            }
        }
        return eName;
    }
}
