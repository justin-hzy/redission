package com.hzy.redission.demo.lock;

import java.util.concurrent.TimeUnit;

public interface RedisLock {

    boolean tryLock(String key, long timeout, TimeUnit unit);

    void releaseLock(String key);
}
