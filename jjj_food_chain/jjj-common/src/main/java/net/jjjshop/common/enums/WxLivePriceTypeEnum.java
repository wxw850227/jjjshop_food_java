package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传枚举
 */
@Getter
@AllArgsConstructor
public enum WxLivePriceTypeEnum {

    //价格类型
    // 1：一口价（只需要传入price，price2不传）
    // 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
    // 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）
    ONE("一口价",1),
    RANGE("价格区间", 2),
    DISCOUNT("显示折扣价",3);


    private String name;
    private Integer value;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        WxLivePriceTypeEnum[] enums = values();    //获取所有枚举集合
        for (WxLivePriceTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
