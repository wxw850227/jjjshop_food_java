

package net.jjjshop.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.interceptor.JjjTenantLineInnerInterceptor;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * MybatisPlus配置
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {
    @Value("${spring.profiles.active}")
    private String env;
    /**
     * spring-boot-jjj配置属性
     */
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new JjjTenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
                // 0为不带租户id
                if(requestDetail == null){
                    return new LongValue(0);
                }
                return new LongValue(requestDetail.getAppId());
            }

            @Override
            public String getTenantIdColumn() {
                // 对应数据库租户标识的列名
                return "app_id";
            }

            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
                List<String> tables = springBootJjjProperties.getIgnoreTables();
                return tables.contains(tableName);
            }
        },env,redisTemplate));
        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
