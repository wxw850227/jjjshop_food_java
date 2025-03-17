package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeductStockTypeEnum {
    CREATE("下单减库存", 10),
    PAYMENT("付款减库存", 20);

    private String name;
    private int value;
}
