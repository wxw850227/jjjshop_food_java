package net.jjjshop.common.enums;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置项枚举
 */
@Getter
@AllArgsConstructor
public enum OrderPayTypeEnum {

    BALANCE("余额支付", 10),
    WECHAT("微信支付", 20);

    private String name;
    private Integer value;

    //查找value集合
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        OrderPayTypeEnum[] enums = values();    //获取所有枚举集合
        for (OrderPayTypeEnum item : enums) {
            values.add(item.getValue());
        }
        return values;
    }

    //查找value集合
    public static List<Integer> getWxValues() {
        List<Integer> values = new ArrayList<>();
        values.add(10);
        values.add(20);
        return values;
    }

    // 查找value集合
    public static Map<Integer, JSONObject> getList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        OrderPayTypeEnum[] enums = values();    //获取所有枚举集合
        for (OrderPayTypeEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            values.put(item.getValue(), obj);
        }
        return values;
    }

    //查找name
    public static String getName(Integer value) {
        String name = null;
        OrderPayTypeEnum[] enums = values();    //获取所有枚举集合
        for (OrderPayTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }

    //查找value集合
    public static List<Integer> getH5Values() {
        List<Integer> values = new ArrayList<>();
        values.add(10);
        return values;
    }
}
