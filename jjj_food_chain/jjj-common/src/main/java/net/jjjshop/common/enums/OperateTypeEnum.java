package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 订单类型枚举
 */
@Getter
@AllArgsConstructor
public enum OperateTypeEnum {
    B2B("B2B2C", 1),
    B2B2C("B2B", 2);

    private String name;
    private Integer value;

    //查找value
    public static Integer getValue(String name) {
        Integer value = 0;
        if(StringUtils.isEmpty(name)){
            return value;
        }
        OperateTypeEnum[] enums = values();    //获取所有枚举集合
        for (OperateTypeEnum item : enums) {
            if(name.equals(item.getName())){
                value = item.getValue();
                break;
            }
        }
        return value;
    }
}
