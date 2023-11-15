package org.aptech.metube.videoservice.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class RedisProperties {
    @Value("${redis.server.host}")
    private String host;
    @Value("${redis.server.port}")
    private int port;
    @Value("${redis.server.password}")
    private String password;
    @Value("${redis.server.serializeContent}")
    private Boolean redisSerializeContent;
}