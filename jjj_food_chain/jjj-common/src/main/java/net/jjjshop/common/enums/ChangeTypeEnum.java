package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传枚举
 */
@Getter
@AllArgsConstructor
public enum ChangeTypeEnum {
    ADMIN("管理员升级",10),
    AUTOUPGRADE("自动升级",20);

    private String name;
    private Integer value;
}
