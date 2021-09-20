package com.atguigu.crowd.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyRedisTest {

    private Logger logger = LoggerFactory.getLogger(MyRedisTest.class);

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void testSet() {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("apple", "red");
    }

    @Test
    public void testExSet() {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("appleEx", "red", 5000, TimeUnit.SECONDS);
    }

}
