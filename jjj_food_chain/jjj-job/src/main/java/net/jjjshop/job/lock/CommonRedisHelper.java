package net.jjjshop.job.lock;


import net.jjjshop.config.properties.SpringBootJjjProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@Component
public class CommonRedisHelper {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 加分布式锁
     *
     * @param track
     * @param sector
     * @param timeout
     * @return
     */
    public boolean setNx(String track, String sector, long timeout) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        Boolean flag = valueOperations.setIfAbsent(this.getCacheKey(track, sector), System.currentTimeMillis());
        // 如果成功设置超时时间, 防止超时
        if (flag) {
            valueOperations.set(this.getCacheKey(track, sector), getLockValue(track, sector), timeout, TimeUnit.SECONDS);
        }
        return flag;
    }

    /**
     * 删除锁
     *
     * @param track
     * @param sector
     */
    public void delete(String track, String sector) {
        redisTemplate.delete(this.getCacheKey(track, sector));
    }

    /**
     * 查询锁
     * @return 写锁时间
     */
    public long getLockValue(String track, String sector) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        long createTime = (long) valueOperations.get(this.getCacheKey(track, sector));
        return createTime;
    }


    private String getCacheKey(String track, String sector){
        return SpringBootJjjProperties.getCachePrefix() + "." + track + sector;
    }

}