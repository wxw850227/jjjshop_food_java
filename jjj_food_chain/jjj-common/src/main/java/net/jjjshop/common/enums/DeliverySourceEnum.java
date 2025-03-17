package net.jjjshop.common.enums;


import com.alibaba.fastjson.JSONObject;
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
public enum DeliverySourceEnum {
    SELLER("商家配送", 10),
    DADA("达达配送", 20),
    DRIVER("配送员配送", 30),
    MEITUAN("美团配送", 40),
    UU("uu跑腿", 50);

    private String name;
    private Integer value;

    //查找value集合
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        DeliverySourceEnum[] enums = values();    //获取所有枚举集合
        for (DeliverySourceEnum item : enums) {
            values.add(item.getValue());
        }
        return values;
    }

    // 查找value集合
    public static Map<Integer, JSONObject> getList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        DeliverySourceEnum[] enums = values();    //获取所有枚举集合
        for (DeliverySourceEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            values.put(item.getValue(), obj);
        }
        return values;
    }

    public static Map<Integer, JSONObject> getTakeoutList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        DeliverySourceEnum[] enums = values();    //获取所有枚举集合
        for (DeliverySourceEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            //就餐类型,10商家配送
            if(item.getValue() == 10){
                values.put(item.getValue(), obj);
            }
        }
        return values;
    }

    //查找name
    public static String getName(Integer value) {
        String name = null;
        DeliverySourceEnum[] enums = values();    //获取所有枚举集合
        for (DeliverySourceEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
