package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传枚举
 */
@Getter
@AllArgsConstructor
public enum WxLiveAuditStatusEnum {
    WAIT("审核中",0),
    PASS("通过",10),
    FAIL("未通过",20);

    private String name;
    private Integer value;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        WxLiveAuditStatusEnum[] enums = values();    //获取所有枚举集合
        for (WxLiveAuditStatusEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
