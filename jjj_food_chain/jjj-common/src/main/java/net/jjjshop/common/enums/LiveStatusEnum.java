package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LiveStatusEnum {
    //直播间状态。0待审核100未通过，101：直播中，102：未开始，103已结束，104：暂停，107：已过期
    TO_AUDIT("待审核", 0),
    AUDIT_FAIL("审核未通过", 100),
    LIVING("直播中", 101),
    WAIT("未开始", 102),
    END("已完成", 103),
    STOP("暂停", 104),
    EXPIRED("已过期", 107);

    private String name;
    private Integer value;

    public static String getName(Integer value) {
        String name = null;
        LiveStatusEnum[] enums = values();    //获取所有枚举集合
        for (LiveStatusEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
