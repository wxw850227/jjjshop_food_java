package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.function.Predicate;

public class MybatisPlusUtils {

    /**
     * 只返回 指定字段
     */
    public static <T> void fieldValues(String fieldValues, LambdaQueryWrapper<T> queryWrapper, Class<T> aClass) {
        if (StringUtils.isNotBlank(fieldValues)) {
            Predicate<TableFieldInfo> predicate = null;
            String[] fieldValuesArray = fieldValues.split(",");
            for (String field : fieldValuesArray) {
                predicate = predicate == null ? p -> p.getColumn().equals(field) : predicate.or(p -> p.getColumn().equals(field));
            }
            queryWrapper.select(aClass, predicate);
        }
    }
}
