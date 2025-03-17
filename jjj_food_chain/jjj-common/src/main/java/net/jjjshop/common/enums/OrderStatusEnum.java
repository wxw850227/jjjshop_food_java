package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    NORMAL("进行中", 10),
    CANCELLED("已取消", 20),
    APPLY_CANCEL("待取消", 21),
    COMPLETED("已完成", 30);

    private String name;
    private Integer value;
}
