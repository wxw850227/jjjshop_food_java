package net.jjjshop.job.lock;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * redis锁拦截器实现
 */
@Aspect
@Slf4j
@Component
public class RedisLockAspect {

    private static final Integer MAX_RETRY_COUNT = 3;
    private static final String LOCK_PRE_FIX = "lockPreFix";
    private static final String LOCK_KEY = "lockKey";
    private static final String TIME_OUT = "timeOut";
    private static final int PROTECT_TIME = 2 << 11;//4096

    @Autowired
    private CommonRedisHelper commonRedisHelper;


    @Pointcut("@annotation(net.jjjshop.job.lock.RedisLock)")
    public void redisLockAspect() {
    }

    @Around("redisLockAspect()")
    public void lockAroundAction(ProceedingJoinPoint proceeding) throws Exception {

        //获取redis锁
        Boolean flag = this.getLock(proceeding, 0, System.currentTimeMillis());
        if (flag) {
            try {
                proceeding.proceed();
                Thread.sleep(PROTECT_TIME);
            } catch (Throwable throwable) {
                throw new RuntimeException("分布式锁执行发生异常" + throwable.getMessage(), throwable);
            } finally {
                // 删除锁
                this.delLock(proceeding);
            }
        } else {
            log.info("其他系统正在执行此项任务");
        }

    }

    /**
     * 获取锁
     *
     * @param proceeding
     * @return
     */
    private boolean getLock(ProceedingJoinPoint proceeding, int count, long currentTime) {
        //获取注解中的参数
        Map<String, Object> annotationArgs = this.getAnnotationArgs(proceeding);
        String lockPrefix = (String) annotationArgs.get(LOCK_PRE_FIX);
        String key = (String) annotationArgs.get(LOCK_KEY);
        long expire = (long) annotationArgs.get(TIME_OUT);
        if (StringUtils.isEmpty(lockPrefix) || StringUtils.isEmpty(key)) {
            // 此条执行不到
            throw new RuntimeException("RedisLock,锁前缀,锁名未设置");
        }
        if (commonRedisHelper.setNx(lockPrefix, key, expire)) {
            return true;
        } else {
            // 如果当前时间与锁的时间差, 大于保护时间,则强制删除锁(防止锁死)
            long createTime = commonRedisHelper.getLockValue(lockPrefix, key);
            if ((currentTime - createTime) > (expire * 1000 + PROTECT_TIME)) {
                count ++;
                if (count > MAX_RETRY_COUNT){
                    return false;
                }
                commonRedisHelper.delete(lockPrefix, key);
                getLock(proceeding,count,currentTime);
            }
            return false;
        }
    }

    /**
     * 删除锁
     *
     * @param proceeding
     */
    private void delLock(ProceedingJoinPoint proceeding) {
        Map<String, Object> annotationArgs = this.getAnnotationArgs(proceeding);
        String lockPrefix = (String) annotationArgs.get(LOCK_PRE_FIX);
        String key = (String) annotationArgs.get(LOCK_KEY);
        commonRedisHelper.delete(lockPrefix, key);
    }

    /**
     * 获取锁参数
     *
     * @param proceeding
     * @return
     */
    private Map<String, Object> getAnnotationArgs(ProceedingJoinPoint proceeding) {
        Class target = proceeding.getTarget().getClass();
        Method[] methods = target.getMethods();
        String methodName = proceeding.getSignature().getName();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Map<String, Object> result = new HashMap<String, Object>();
                RedisLock redisLock = method.getAnnotation(RedisLock.class);
                result.put(LOCK_PRE_FIX, redisLock.lockPrefix());
                result.put(LOCK_KEY, redisLock.lockKey());
                result.put(TIME_OUT, redisLock.timeUnit().toSeconds(redisLock.timeOut()));
                return result;
            }
        }
        return null;
    }

}