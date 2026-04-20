package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void processNotifications() {

        String listKey = "user:1:notifications"; // for now static

        Long size = redisTemplate.opsForList().size(listKey);

        if (size != null && size > 0) {
            System.out.println("Summary: " + size + " new interactions");

            redisTemplate.delete(listKey);
        }
    }
}
