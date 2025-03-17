package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponColorEnum {
    BLUE("blue", 10),
    RED("red", 20),
    PURPLE("violet", 30),
    YELLOW("yellow", 40);

    private String text;
    private Integer value;

    //查找name
    public static String getText(Integer value) {
        String text = null;
        CouponColorEnum [] enums = values();    //获取所有枚举集合
        for (CouponColorEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                text = item.getText();
                break;
            }
        }
        return text;
    }
}
