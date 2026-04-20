package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void updateViralityScore(Long postid, String type){
        String key = "post:" + postid + ":score";
        int value = switch (type){
            case "LIKE" -> 1;
            case "COMMENT" ->50;
            case "BOT"->1;
            default -> 0;
        };
        redisTemplate.opsForValue().increment(key,value);
    }

    public boolean checkCooldown(Long botId, Long userId) {
        String key = "cooldown:bot_" + botId + ":user_" + userId;
        Boolean exists = redisTemplate.hasKey(key);
        if (exists != null && exists) {
            return false;
        }
        redisTemplate.opsForValue().set(key, "1", 10, TimeUnit.MINUTES);
        return true;
    }

    public boolean canBotReply(Long postId) {
        String key = "post:" + postId + ":bot_count";
        Long count = redisTemplate.opsForValue().increment(key);
        return count <= 100;
    }

    public void handleNotification(Long userId, String message) {

        String cooldownKey = "notif:cooldown:" + userId;
        String listKey = "user:" + userId + ":notifications";
        Boolean exists = redisTemplate.hasKey(cooldownKey);

        if (exists != null && exists) {
            redisTemplate.opsForList().rightPush(listKey, message);
        } else {
            System.out.println("Push Notification Sent: " + message);
            redisTemplate.opsForValue().set(cooldownKey, "1", 15, TimeUnit.MINUTES);
        }
    }
}
