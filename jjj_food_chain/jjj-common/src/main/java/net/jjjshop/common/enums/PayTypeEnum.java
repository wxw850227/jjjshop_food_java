package net.jjjshop.common.enums;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum PayTypeEnum {

    WECHAT("微信支付", 10),
    ALIPAY("支付宝", 20),
    CARD("银行卡", 30);

    private String name;
    private Integer value;

    //查找value
    public static Integer getValue(String name) {
        Integer value = 0;
        if(StringUtils.isEmpty(name)){
            return value;
        }
        PayTypeEnum[] enums = values();    //获取所有枚举集合
        for (PayTypeEnum item : enums) {
            if(name.equals(item.getName())){
                value = item.getValue();
                break;
            }
        }
        return value;
    }

    public static String getName(Integer value) {
        String name = null;
        PayTypeEnum[] enums = values();    //获取所有枚举集合
        for (PayTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }

    public static List<JSONObject> getList() {
        List<JSONObject> list = new ArrayList<>();
        PayTypeEnum[] enums = values();
        for (PayTypeEnum item : enums) {
            JSONObject json = new JSONObject();
            json.put("name",item.getName());
            json.put("value", item.getValue());
            list.add(json);
        }
        return list;
    }
}
