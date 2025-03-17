package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单支付类型枚举
 */
@Getter
@AllArgsConstructor
public enum OrderPayStatusEnum {

    PENDING("待付款", 10),
    SUCCESS("已付款", 20);

    private String name;
    private Integer value;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        OrderPayStatusEnum[] enums = values();    //获取所有枚举集合
        for (OrderPayStatusEnum item : enums) {
            if (item.getValue().intValue() == value.intValue()) {
                name = item.getName();
                break;
            }
        }
        return name;
    }

}
