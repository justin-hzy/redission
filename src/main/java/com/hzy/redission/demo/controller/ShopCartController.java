package com.hzy.redission.demo.controller;

import com.hzy.redission.demo.service.ServiceA;
import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ShopCartController {

    @Autowired
    private Redisson redisson;

    /*@Autowired
    private RedissonLock redissonLock;*/

    @Autowired
    private StringRedisTemplate redisTemplate;

    protected static final String product = "123456789";

    @RequestMapping("/submitOrder")
    public String submitOrder(){
        //业务没有保证原子性
        Boolean  lock = redisTemplate.opsForValue().setIfAbsent(product,"wuji",30, TimeUnit.SECONDS);//setnx
        if (!lock){
            return "error";
        }
        int stock = Integer.parseInt(redisTemplate.opsForValue().get("stock")); //jedis.get
        if (stock>0){
            stock -=1;
            /* 其他业务代码 begin*/
            ServiceA serviceA = new ServiceA();
            serviceA.methodA();
            /* 其他业务代码 end*/
            redisTemplate.opsForValue().set("stock",stock+"");// jedis.set
            System.out.println(Thread.currentThread().getId() +"扣减成功，库存stock："+stock);
        }else {
            System.out.println("扣减失败，库存不足");
            return "error";
        }
        redisTemplate.delete(product);
        return "end";
    }
}
