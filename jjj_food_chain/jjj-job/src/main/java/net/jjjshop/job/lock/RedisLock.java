package net.jjjshop.job.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * redis锁注解
 * @author wangxw
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RedisLock {

    String lockPrefix() default "";
    String lockKey() default "";
    long timeOut() default 5;
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}