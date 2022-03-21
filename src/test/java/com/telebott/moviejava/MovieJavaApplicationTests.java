package com.telebott.moviejava;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class MovieJavaApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
//    private TimeUtil timeUtil;
    @Test
    void contextLoads() {
        System.out.println(TimeUtil.getTodayZero());
    }

}
