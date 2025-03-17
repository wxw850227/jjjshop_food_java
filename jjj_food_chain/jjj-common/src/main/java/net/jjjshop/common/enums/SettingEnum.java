package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设置项枚举
 */
@Getter
@AllArgsConstructor
public enum SettingEnum {
    /**
     * 系统设置
     **/
    SYS_CONFIG("sys_config", "系统设置", "SysConfigVo"),
    STORE("store", "商城设置", "StoreVo"),
    STORAGE("storage", "上传设置", "StorageVo"),
    SMS("sms", "短信设置", "SmsVo"),
    TRADE("trade", "交易设置", "TradeVo"),
    MPSERVICE("mp_service", "客服设置", "MpServiceVo"),
    PRINTING("printer", "打印机设置", "PrinterVo"),
    DELIVER("deliver", "配送设置", "DeliverVo"),
    GETPHONE("getPhone", "获取手机号", "GetPhoneVo"),
    POINTS("points", "积分设置", "PointsVo"),
    HOMEPUSH("homepush","首页推送","HomePushVo"),
    COLLECTION("collection","引导收藏","CollectionVo"),
    RECOMMEDND("recommend","商品推荐","RecommendVo"),
    APP_SHARE("app_share", "app分享设置", "AppShareVo"),
    THEME("theme", "主题设置", "ThemeVo"),
    PAGE_CATEGORY("page_category", "分类设置", "PageCategoryVo"),
    NAV("nav", "底部导航", "NavVo"),
    FULL_FREE("full_reduce","满额包邮设置","FullFreeVo"),
    SIGN("sign","签到有礼设置","SignVo"),
    INVITATION("invitation", "邀请有礼设置","InvitationVo"),
    POINTSMALL("pointsMall","积分商城设置","PointsMallVo"),
    ASSEMBLE("assemble", "拼团砍价设置", "AssembleVo"),
    BARGAIN("bargain","砍价设置","BargainVo"),
    SECKILL("seckill","秒杀设置","SeckillVo"),
    CARD("card","提货码设置", "CardVo"),
    BALANCE("balance", "余额充值设置", "BalanceVo"),
    BALANCECASH("balance_cash","余额提现设置","BalanceCashVo"),
    TASK("task","任务中心","TaskVo"),
    LIVE("live","APP直播设置","LiveVo"),
    ADVANCE("advance","预售活动设置","AdvanceVo"),
    TX_MAP("tx_map","腾讯地图设置","TxMapVo"),
    PROTOCOL("protocol","协议设置","ProtocolVo"),
    OFFICIA("officia","公众号关注","OfficiaVo");


    private String key;
    private String description;
    private String className;

    //根据传入的key动态获取className
    public static String getClassNameByKey(String key) {
        SettingEnum[] enums = values();    //获取所有枚举集合
        for (SettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getClassName();
            }
        }
        return null;
    }

    //根据传入的key动态获取description
    public static String getDescriptionByKey(String key) {
        SettingEnum[] enums = values();    //获取所有枚举集合
        for (SettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getDescription();
            }
        }
        return null;
    }
}
