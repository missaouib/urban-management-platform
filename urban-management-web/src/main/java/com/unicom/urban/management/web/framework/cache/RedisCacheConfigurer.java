package com.unicom.urban.management.web.framework.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * redis缓存配置
 *
 * @author liukai
 */
@Configuration
public class RedisCacheConfigurer {


//    @Bean
//    public CacheManager cacheManager() {

//        return new EhCacheCacheManager();

    //缓存配置对象
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                //设置缓存的默认超时时间：30分钟
//                .entryTtl(Duration.ofMinutes(30L))
//                //如果是空值，不缓存
//                .disableCachingNullValues()
//                //设置key序列化器
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
//                //设置value序列化器
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((valueSerializer())));
//        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory)).cacheDefaults(cacheConfig).build();
//    }

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory)).build();
//    }


}
