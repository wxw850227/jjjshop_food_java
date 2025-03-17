package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传枚举
 */
@Getter
@AllArgsConstructor
public enum LiveRoomStatusEnum {
    audit("待审核", 0),
    failed("审核失败",  100),
    living("直播中", 101),
    close("已结束", 103),
    pause("暂停中", 104),
    timeout("已过期", 107);

    private String name;
    private Integer value;

    //查找name
    public static String getName(Integer value) {
        String name = null;
        LiveRoomStatusEnum[] enums = values();    //获取所有枚举集合
        for (LiveRoomStatusEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
