package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponTypeEnum {

    FULL_MINUS("满减券",10),
    DISCOUNT("折扣券", 20);

    private String name;
    private int value;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        CouponTypeEnum[] enums = values();    //获取所有枚举集合
        for (CouponTypeEnum item : enums) {
            if(item.getValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }

}
