package com.hzy.redission.demo.service;

import com.hzy.redission.demo.lock.RedisLock;

import java.util.concurrent.TimeUnit;

public class ServiceB {

    public RedisLock redisLock;

    public void methodB(){
        String key = "123456789";
        boolean lock = redisLock.tryLock(key,30, TimeUnit.SECONDS);
        if(!lock){
            return;
        }
        try {
            // .....业务代码
        }finally {
            redisLock.releaseLock(key);
        }
    }
}
