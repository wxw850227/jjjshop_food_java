

package net.jjjshop.framework.common.enums;


/**
 * 枚举类型父接口
 */
public interface BaseNameEnum {

    /**
     * 获取枚举标识
     *
     * @return
     */
    String getCode();

    /**
     * 获取枚举描述
     *
     * @return
     */
    String getDesc();

    /**
     * 通过枚举类型和code值获取对应的枚举类型
     * @param enumType
     * @param code
     * @param <T>
     * @return
     */
    static <T extends BaseNameEnum> T valueOf(Class<? extends BaseNameEnum> enumType, String code) {
        if (enumType == null || code == null) {
            return null;
        }
        T[] enumConstants = (T[]) enumType.getEnumConstants();
        if (enumConstants == null) {
            return null;
        }
        for (T enumConstant : enumConstants) {
            String enumCode = enumConstant.getCode();
            if (code.equals(enumCode)) {
                return enumConstant;
            }
        }
        return null;
    }

}
