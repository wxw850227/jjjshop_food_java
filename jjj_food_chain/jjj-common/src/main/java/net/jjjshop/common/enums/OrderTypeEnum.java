package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {
    MASTER("商城订单", 10, "Master"),
    GIFT("礼包购订单", 20, "Gift"),
    BALANCE("余额充值订单", 30, "Balance");

    private String name;
    private Integer value;
    private String eName;

    //查找eName
    public static String getEName(Integer value) {
        String eName = null;
        OrderTypeEnum[] enums = values();    //获取所有枚举集合
        for (OrderTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                eName = item.getEName();
                break;
            }
        }
        return eName;
    }
}
