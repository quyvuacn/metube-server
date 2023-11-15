package org.aptech.metube.videoservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aptech.metube.videoservice.config.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {
    @Autowired
    RedisProperties redisProperties;

    @Bean
    JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = null;
        JedisPoolConfig p = new JedisPoolConfig();
        // standalone vs replicate (node master vs slave)
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        JedisClientConfiguration.JedisClientConfigurationBuilder clientBuilder
                = JedisClientConfiguration.builder();
        clientBuilder.usePooling().poolConfig(p);
        jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                clientBuilder.build());
        return jedisConnectionFactory;
    }
    @Bean
    @Primary
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);

        if (redisProperties.getRedisSerializeContent()){
            // serialize value in redis
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                    = new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper objectMapper = new ObjectMapper(); // chuyển từ Json => String và ngược lại
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //  chỉ serialize trường notnull
            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        }
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            configurationMap.put("default", RedisCacheConfiguration.defaultCacheConfig()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer
                            (new GenericJackson2JsonRedisSerializer())));
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }
}
