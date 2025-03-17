

package net.jjjshop.framework.shiro.util;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.properties.JwtProperties;
import net.jjjshop.framework.util.HttpServletRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * JwtToken工具类
 **/
@Slf4j
@Component
public class JwtTokenUtil {

    private static String tokenName;

    public JwtTokenUtil(JwtProperties jwtProperties) {
        tokenName = jwtProperties.getTokenName();
        log.debug("tokenName:{}", tokenName);
    }

    /**
     * 获取token名称
     *
     * @return
     */
    public static String getTokenName(String model) {
        if(model.equals("")){
            return tokenName;
        }else{
            return tokenName + "" + model;
        }
    }

    /**
     * 从请求头或者请求参数中
     *
     * @return
     */
    public static String getToken(String model) {
        return getToken(HttpServletRequestUtil.getRequest(), model);
    }

    public static String getModel(String path){
        String model = "";
        if(path.startsWith("/api/admin/")){
            model = "admin";
        }else if(path.startsWith("/api/shop/")){
            model = "shop";
        }
        return model;
    }

    /**
     * 从请求头或者请求参数中
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request, String model) {
        if (request == null) {
            throw new IllegalArgumentException("request不能为空");
        }
        String realTokenName = "";
        if(model.equals("")){
            realTokenName = tokenName;
        }else{
            realTokenName = tokenName + "" + model;
        }
        // 从请求头中获取token
        String token = request.getHeader(realTokenName);
        if (StringUtils.isBlank(token)) {
            // 从请求参数中获取token
            token = request.getParameter(realTokenName);
        }
        return token;
    }
}
