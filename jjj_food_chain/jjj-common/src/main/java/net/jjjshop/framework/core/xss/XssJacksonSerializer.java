

package net.jjjshop.framework.core.xss;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Jackson响应参数字符串转义处理
 **/
@Slf4j
public class XssJacksonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (isJson(s)) {
            jsonGenerator.writeString(s);
            return;
        }
        jsonGenerator.writeString(StringEscapeUtils.unescapeHtml4(s));
    }

    /**
     * 判断字符串是不是JSON
     *
     * @param str
     * @return
     */
    private boolean isJson(String str) {
        boolean result = false;
        if (!StringUtils.isEmpty(str)) {
            str = str.trim();
            if (str.startsWith("{") && str.endsWith("}")) {
                result = true;
            } else if (str.startsWith("[") && str.endsWith("]")) {
                result = true;
            }
        }
        return result;
    }
}
