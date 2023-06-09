package cn.wr1sw.lottery.common.service;


import cn.wr1sw.lottery.common.config.RedisConfig;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description Redis工具类
 */
@Service
@AllArgsConstructor
public class RedisService {

    private RedisTemplate<String, Serializable> redisTemplate;

    private RedisConfig redisConfig;

    /**
     * 写入普通缓存，key由系统生成
     *
     * @param value 值
     * @return 键
     */
    public String set(Serializable value) {
        // 随机生成key
        String key = UUID.randomUUID().toString();
        // 如果当前生成的key已经存在，重新生成
        if (hasKey(key)) {
            return set(value);
        } else {
            // 写入缓存
            redisTemplate.opsForValue().set(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key, value);
            // 返回key
            return key;
        }
    }

    /**
     * 写入带过期时间的缓存，其中key由系统生成
     *
     * @param value      值
     * @param expireTime 过期时间
     * @return 键key
     */
    public String set(Serializable value, Long expireTime) {
        // 随机生成key
        String key = UUID.randomUUID().toString();
        // 如果当前随机生成的key已经存在，则重新生成
        if (hasKey(key)) {
            return set(value, expireTime);
        } else {
            // 写入缓存
            redisTemplate.opsForValue().set(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key, value, expireTime, TimeUnit.SECONDS);
            // 返回key
            return key;
        }
    }

    /**
     * 写入普通缓存，指定key
     *
     * @param key   键
     * @param value 值
     * @return 结果
     */
    public boolean set(String key, Serializable value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入带过期时间的缓存，指定key
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间
     * @return 结果
     */
    public boolean set(String key, Serializable value, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key, value, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断某个键是否存在
     *
     * @param key 键
     * @return 存在与否
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key));
    }

    /**
     * 通过键获取值
     *
     * @param key 缓存的键
     * @return 存在：值 | 不存在：null
     */
    public Serializable get(String key) {
        if (hasKey(key)) {
            return redisTemplate.opsForValue().get(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key);
        }
        return null;
    }

    /**
     * 获取某个键值的过期时间
     *
     * @param key 键
     * @return 过期时间（秒），-1表示永久有效，-2表示不存在
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key, TimeUnit.SECONDS);
    }

    /**
     * 通过键移出某个值
     *
     * @param key 键
     */
    public void remove(String key) {
        if (hasKey(key)) {
            redisTemplate.delete(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key);
        }
    }

    /**
     * 删除包含指定前缀的值
     *
     * @param prefix 键
     */
    public void removeByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys((redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + prefix : prefix) + "*");
        if (null != keys && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }


    /**
     * 通过键移出某些值
     *
     * @param keys 批量的键，示例：remove("001","002","003")
     */
    public void remove(String... keys) {
        for (String key : keys) {
            remove(redisConfig.getAllowPrefix() ? redisConfig.getPrefix() + key : key);
        }

    }
}
