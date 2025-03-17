package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传枚举
 */
@Getter
@AllArgsConstructor
public enum WxLiveProductAuditStatusEnum {

    //审核状态：0：未审核，1：审核中，2:审核通过，3审核失败
    WAIT("未审核",0),
    REVIEW("审核中",1),
    PASS("审核通过", 2),
    FAIL("未通过",3);


    private String name;
    private Integer value;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        WxLiveProductAuditStatusEnum[] enums = values();    //获取所有枚举集合
        for (WxLiveProductAuditStatusEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
