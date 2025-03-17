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
public enum DeliveryTypeEnum {
    //就餐类型,10外卖配送,20外卖自提,30店内打包40店内堂食
    WAIMAI("外卖配送", 10),
    ZITI("外卖自提", 20),
    DABAO("店内打包", 30),
    TANGSHI("店内堂食", 40);

    private String name;
    private Integer value;

    //查找value集合
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        DeliveryTypeEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryTypeEnum item : enums) {
            values.add(item.getValue());
        }
        return values;
    }

    // 查找value集合
    public static Map<Integer, JSONObject> getList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        DeliveryTypeEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryTypeEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            values.put(item.getValue(), obj);
        }
        return values;
    }

    public static Map<Integer, JSONObject> getStoreList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        DeliveryTypeEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryTypeEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            //就餐类型,10外卖配送,20外卖自提,30店内打包40店内堂食
            if(item.getValue() == 30 || item.getValue() == 40){
                values.put(item.getValue(), obj);
            }
        }
        return values;
    }

    public static Map<Integer, JSONObject> getTakeoutList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        DeliveryTypeEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryTypeEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            //就餐类型,10外卖配送,20外卖自提,30店内打包40店内堂食
            if(item.getValue() == 10 || item.getValue() == 20){
                values.put(item.getValue(), obj);
            }
        }
        return values;
    }

    public static List<JSONObject> getDeliveryList() {
        List<JSONObject> list = new ArrayList<>();
        DeliveryTypeEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryTypeEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            list.add(obj);
        }
        return list;
    }

    //查找name
    public static String getName(Integer value) {
        String name = null;
        DeliveryTypeEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
