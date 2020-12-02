package com.hzy.redission.demo.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLockImpl implements RedisLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        boolean lock = false;
        if (threadLocal.get()==null){
            String uuid = UUID.randomUUID().toString().replace("-","");
            threadLocal.set(uuid);
            lock = stringRedisTemplate.opsForValue().setIfAbsent(key,"wuji",timeout,unit);
        }else {
            lock = true;
        }
        return lock;
    }

    @Override
    public void releaseLock(String key) {
        if(threadLocal.get().equals(stringRedisTemplate.opsForValue().get(key))){
            stringRedisTemplate.delete(key);
            threadLocal.remove();
        }

    }
}
