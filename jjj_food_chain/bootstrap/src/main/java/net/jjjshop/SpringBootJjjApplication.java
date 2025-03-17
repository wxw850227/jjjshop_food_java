

package net.jjjshop;

import net.jjjshop.config.JjjNameGenerator;
import net.jjjshop.framework.util.PrintApplicationInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * spring-boot-jjj 项目启动入口
 */
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
@ComponentScan(nameGenerator = JjjNameGenerator.class)
@ServletComponentScan
@MapperScan(basePackages = {"net.jjjshop.**.mapper"}, nameGenerator = JjjNameGenerator.class)
@SpringBootApplication(scanBasePackages = {"net.jjjshop"})
public class SpringBootJjjApplication {

    public static void main(String[] args) {
        // 启动spring-boot-jjj
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootJjjApplication.class, args);
        // 打印项目信息
        PrintApplicationInfo.print(context);
        // 打印项目提示
        PrintApplicationInfo.printTip(context);
    }

}
