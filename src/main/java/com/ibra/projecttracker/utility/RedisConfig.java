package com.ibra.projecttracker.utility;//package com.ibra.projecttracker.utility;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10)) // Set TTL
//                .disableCachingNullValues();
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfiguration)
//                .build();
//    }
////
////    @Bean
////    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
////        RedisTemplate<String, Object> template = new RedisTemplate<>();
////        template.setConnectionFactory(connectionFactory);
////
//    ////        // Use String serializer for keys
//    ////        template.setKeySerializer(new StringRedisSerializer());
//    ////
//    ////        // Use Jackson JSON serializer for values
//    ////        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//    ////        template.setEnableTransactionSupport(true);
//    ////
//    ////        return template;
////        // Configure ObjectMapper with proper modules
////        ObjectMapper mapper = new ObjectMapper()
////                .registerModule(new JavaTimeModule())
////                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
////
////        GenericJackson2JsonRedisSerializer jsonSerializer =
////                new GenericJackson2JsonRedisSerializer(mapper);
////
////        template.setDefaultSerializer(jsonSerializer);
////        template.setKeySerializer(new StringRedisSerializer());
////        template.setValueSerializer(jsonSerializer);
////        template.setHashKeySerializer(new StringRedisSerializer());
////        template.setHashValueSerializer(jsonSerializer);
////
////        return template;
////    }
////
////    @Bean
////    public GenericJackson2JsonRedisSerializer jacksonSerializer() {
////        ObjectMapper objectMapper = new ObjectMapper();
////        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        objectMapper.activateDefaultTyping(
////                objectMapper.getPolymorphicTypeValidator(),
////                ObjectMapper.DefaultTyping.NON_FINAL,
////                JsonTypeInfo.As.WRAPPER_ARRAY
////        );
////
////        return new GenericJackson2JsonRedisSerializer(objectMapper);
////    }
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory();
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
//
//}

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer; // Import this
import com.fasterxml.jackson.databind.ObjectMapper; // May need to inject ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature; // For pretty printing if desired
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // For LocalDateTime support

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    // You might want to provide your own ObjectMapper to customize its behavior
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Makes JSON human-readable (pretty print)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Important for LocalDateTime
        mapper.registerModule(new JavaTimeModule()); // Support for Java 8 Date/Time API
        return mapper;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration(ObjectMapper objectMapper) { // Inject ObjectMapper
        // Create a specific Jackson2JsonRedisSerializer with your custom ObjectMapper
        // This ensures the JSON is stored as a String (UTF-8) in Redis
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // Set a default TTL (Time To Live) for cache entries
                .disableCachingNullValues() // Don't cache null values
                // --- CRUCIAL CHANGE FOR READABILITY ---
                // Use StringRedisSerializer for keys to make them readable (e.g., "projects::123")
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                // Use your custom Jackson2JsonRedisSerializer for values to store them as readable JSON strings
                .serializeValuesWith(SerializationPair.fromSerializer(jsonSerializer));
    }
}
