package net.jjjshop.common.util.lock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Component;


import java.util.Objects;


@Component
@Slf4j
@Data
public class RedisLock {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取锁，true 则得到锁，false 已被锁定
     * @param lockName       锁名称
     * @param lockExoire     锁时间
     * @return
     */
    public Boolean getLock(String lockName, Integer lockExoire) {
        return (Boolean) redisTemplate.execute((RedisCallback<?>) connection -> {
            // 获取时间毫秒值
            long expireAt = System.currentTimeMillis() + lockExoire + 1;
            // 获取锁
            Boolean acquire = connection.setNX(lockName.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {
                byte[] bytes = connection.get(lockName.getBytes());
                // 非空判断
                if (Objects.nonNull(bytes) && bytes.length > 0) {
                    long expireTime = Long.parseLong(new String(bytes));
                    // 如果锁已经过期
                    if (expireTime < System.currentTimeMillis()) {
                        // 重新加锁，防止死锁
                        byte[] set = connection.getSet(lockName.getBytes(),
                                String.valueOf(System.currentTimeMillis() + lockExoire + 1).getBytes());
                        return Long.parseLong(new String(set)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     * @param lockName
     */
    public void delLock(String lockName) {
        redisTemplate.delete(lockName);
    }

    /**
     * 获取锁Key
     * @param prefix    前缀
     * @param name      名称
     * @return
     */
    public static String getFullKey(String prefix, String name) {
        return prefix + "_" + name;
    }
}



