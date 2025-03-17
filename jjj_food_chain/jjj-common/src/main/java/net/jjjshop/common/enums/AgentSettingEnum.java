package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置项枚举
 */
@Getter
@AllArgsConstructor
public enum AgentSettingEnum {
    /**
     * 系统设置
     **/
    BASIC("basic", "基础设置", "BasicVo"),
    CONDITION("condition", "分销商条件", "ConditionVo"),
    COMMISSION("commission", "佣金设置", "CommissionVo"),
    SETTLEMENT("settlement", "结算", "SettlementVo"),
    WORDS("words", "自定义文字", "WordsVo"),
    LICENSE("license", "申请协议", "LicenseVo"),
    BACKGROUND("background", "页面背景图", "BackgroundVo"),
    QRCODE("qrcode","海报设置","QrcodeVo"),
    TEMPLATE_MSG("template_msg", "模板消息", "TemplateMsgVo");

    private String key;
    private String description;
    private String className;

    // 查找key集合
    public static List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        AgentSettingEnum[] enums = values();    //获取所有枚举集合
        for (AgentSettingEnum item : enums) {
            keys.add(item.getKey());
        }
        return keys;
    }

    //根据传入的key动态获取className
    public static String getClassNameByKey(String key) {
        AgentSettingEnum[] enums = values();    //获取所有枚举集合
        for (AgentSettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getClassName();
            }
        }
        return null;
    }

    //根据传入的key动态获取description
    public static String getDescriptionByKey(String key) {
        AgentSettingEnum[] enums = values();    //获取所有枚举集合
        for (AgentSettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getDescription();
            }
        }
        return null;
    }
}
