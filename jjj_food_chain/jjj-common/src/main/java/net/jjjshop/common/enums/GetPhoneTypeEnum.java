package net.jjjshop.common.enums;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum GetPhoneTypeEnum {

    GEREN("个人中心", 10);


    private String name;
    private Integer value;

    //查找value
    public static Integer getValue(String name) {
        Integer value = 0;
        if(StringUtils.isEmpty(name)){
            return value;
        }
        GetPhoneTypeEnum[] enums = values();    //获取所有枚举集合
        for (GetPhoneTypeEnum item : enums) {
            if(name.equals(item.getName())){
                value = item.getValue();
                break;
            }
        }
        return value;
    }

    public static String getName(Integer value) {
        String name = null;
        GetPhoneTypeEnum[] enums = values();    //获取所有枚举集合
        for (GetPhoneTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }

    public static List<JSONObject> getList() {
        List<JSONObject> list = new ArrayList<>();
        GetPhoneTypeEnum[] enums = values();
        for (GetPhoneTypeEnum item : enums) {
            JSONObject json = new JSONObject();
            json.put("text",item.getName());
            json.put("value", item.getValue());
            list.add(json);
        }
        return list;
    }

    public static List<Integer> getValueList() {
        List<Integer> list = new ArrayList<>();
        GetPhoneTypeEnum[] enums = values();
        for (GetPhoneTypeEnum item : enums) {
            list.add(item.getValue());
        }
        return list;
    }
}
