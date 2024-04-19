package com.project.goalchallenge.global.config.redis;

import java.util.HashMap;
import java.util.Map;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class RedissonConfig {

  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private int redisPort;

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
    config.setCodec(new JsonJacksonCodec());
    return Redisson.create(config);
  }

  @Bean
  public RedissonSpringCacheManager cacheManager(RedissonClient redissonClient) {
    Map<String, CacheConfig> configMap = new HashMap<>();

    configMap.put("challengeParticipantsDuplicateCache", new CacheConfig(60 * 60 * 1000, 10 * 60 * 1000));
    return new RedissonSpringCacheManager(redissonClient, configMap);
  }

}
