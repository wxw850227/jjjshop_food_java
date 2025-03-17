

package net.jjjshop.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.config.properties.SpringBootJjjFilterProperties;
import net.jjjshop.config.properties.SpringBootJjjInterceptorProperties;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.core.filter.RequestDetailFilter;
import net.jjjshop.framework.core.interceptor.FrontInterceptor;
import net.jjjshop.framework.core.interceptor.PermissionInterceptor;
import net.jjjshop.framework.core.xss.XssFilter;
import net.jjjshop.framework.util.IniUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * WebMvc配置
 */
@Slf4j
@Configuration
public class SpringBootJjjWebMvcConfig implements WebMvcConfigurer {

    /**
     * spring-boot-jjj配置属性
     */
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;

    /**
     * Filter配置
     */
    private SpringBootJjjFilterProperties filterConfig;

    /**
     * 拦截器配置
     */
    private SpringBootJjjInterceptorProperties interceptorConfig;

    /**
     * RequestDetailFilter配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean requestDetailFilter() {
        SpringBootJjjFilterProperties.FilterConfig requestFilterConfig = filterConfig.getRequest();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RequestDetailFilter());
        filterRegistrationBean.setEnabled(requestFilterConfig.isEnable());
        filterRegistrationBean.addUrlPatterns(requestFilterConfig.getUrlPatterns());
        filterRegistrationBean.setOrder(requestFilterConfig.getOrder());
        filterRegistrationBean.setAsyncSupported(requestFilterConfig.isAsync());
        return filterRegistrationBean;
    }

    /**
     * XssFilter配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean xssFilter() {
        SpringBootJjjFilterProperties.FilterConfig xssFilterConfig = filterConfig.getXss();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setEnabled(xssFilterConfig.isEnable());
        filterRegistrationBean.addUrlPatterns(xssFilterConfig.getUrlPatterns());
        filterRegistrationBean.setOrder(xssFilterConfig.getOrder());
        filterRegistrationBean.setAsyncSupported(xssFilterConfig.isAsync());
        return filterRegistrationBean;
    }


    /**
     * 自定义权限拦截器
     *
     * @return
     */
    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    }

    /**
     * 前端拦截器
     *
     * @return
     */
    @Bean
    public FrontInterceptor frontInterceptor() {
        return new FrontInterceptor();
    }

    @PostConstruct
    public void init() {
        filterConfig = springBootJjjProperties.getFilter();
        interceptorConfig = springBootJjjProperties.getInterceptor();
        // 打印SpringBootJjjProperties配置信息
        log.debug("SpringBootJjjProperties：{}", JSON.toJSONString(springBootJjjProperties));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 自定义权限拦截器注册
        if (interceptorConfig.getPermission().isEnable()) {
            registry.addInterceptor(permissionInterceptor())
                    .addPathPatterns(interceptorConfig.getPermission().getIncludePaths())
                    .excludePathPatterns(interceptorConfig.getPermission().getExcludePaths());
        }
        registry.addInterceptor(frontInterceptor())
                .addPathPatterns("/front/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置项目静态资源访问
        String resourceHandlers = springBootJjjProperties.getResourceHandlers();
        if (StringUtils.isNotBlank(resourceHandlers)) {
            Map<String, String> map = IniUtil.parseIni(resourceHandlers);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String pathPatterns = entry.getKey();
                String resourceLocations = entry.getValue();
                registry.addResourceHandler(pathPatterns)
                        .addResourceLocations(resourceLocations);
            }
        }

        // 设置上传文件访问路径
        registry.addResourceHandler(springBootJjjProperties.getResourceAccessPatterns())
                .addResourceLocations("file:" + springBootJjjProperties.getUploadPath() + "/");
    }

}
