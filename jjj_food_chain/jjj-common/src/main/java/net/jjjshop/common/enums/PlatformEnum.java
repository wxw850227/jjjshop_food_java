package net.jjjshop.common.enums;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付平台枚举
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum {

    H5("H5", "h5", OrderPayTypeEnum.getH5Values()),
    WX("微信小程序", "wx", OrderPayTypeEnum.getWxValues());

    private String name;
    private String value;
    private List<Integer> payType;

    // 查找value集合
    public static Map<String, JSONObject> getList() {
        Map<String, JSONObject> values = new HashMap<>();
        PlatformEnum[] enums = values();    //获取所有枚举集合
        for (PlatformEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            obj.put("payType", item.getPayType());
            values.put(item.getValue(), obj);
        }
        return values;
    }

    //查找payType
    public static List<Integer> getPayType(String value) {
        List<Integer> payType = null;
        PlatformEnum[] enums = values();    //获取所有枚举集合
        for (PlatformEnum item : enums) {
            if(item.getValue().equals(value)){
                payType = item.getPayType();
                break;
            }
        }
        return payType;
    }
}
