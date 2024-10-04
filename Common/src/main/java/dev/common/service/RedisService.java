package dev.common.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson;
    private final int TIME_EXPIRATION = 10;

    public void setValue(String key, Object data){
        redisTemplate.opsForValue().set(key, gson.toJson(data), TIME_EXPIRATION, TimeUnit.MINUTES);
    }

    public Object getValue(String key, Class<? extends Object> clazz){
        Object value = redisTemplate.opsForValue().get(key);
        if(value == null)
            return null;
        return gson.fromJson(value.toString(), clazz);
    }

    public void deleteValue(String key){
        redisTemplate.delete(key);
    }
}