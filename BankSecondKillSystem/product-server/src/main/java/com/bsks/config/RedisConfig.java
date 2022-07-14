package com.bsks.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.net.UnknownHostException;

@Configuration
public class RedisConfig {

    @Bean
    public DefaultRedisScript<String> reduceScript() {
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(String.class);
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/reduceProduct.lua")));
        return defaultRedisScript;
    }

    @Bean
    public DefaultRedisScript<String> increaseScript() {
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(String.class);
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/increaseProduct.lua")));
        return defaultRedisScript;
    }

    @Bean
    public RedisTemplate<String,Object> myRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException{
        RedisTemplate<String,Object> template = new RedisTemplate<> ();
        Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object> (Object.class);
        ObjectMapper om  = new ObjectMapper();
        om.setVisibility (PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping (ObjectMapper.DefaultTyping.NON_FINAL);
        objectJackson2JsonRedisSerializer.setObjectMapper (om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer (  );
        template.setKeySerializer (stringRedisSerializer);
        template.setHashKeySerializer (stringRedisSerializer);
        template.setValueSerializer (objectJackson2JsonRedisSerializer);
        template.setHashValueSerializer (objectJackson2JsonRedisSerializer);
        template.setConnectionFactory (redisConnectionFactory);
        template.afterPropertiesSet ();
        return template;
    }
}
