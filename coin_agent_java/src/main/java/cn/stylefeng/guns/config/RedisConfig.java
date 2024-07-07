package cn.stylefeng.guns.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * redis配置： RedisConfig文件名不能修改
 */
@Configuration
public class RedisConfig {

	/**
	 * 配置自定义redisTemplate
	 * 因为使用的连接客户端为：Lettuce,所以RedisConnectionFactory实际传入数据为 LettuceConnectionFactory
	 *
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

//	/**
//	 * json序列化
//	 *
//	 * @return
//	 */
//	@Bean
//	public RedisSerializer<Object> jackson2JsonRedisSerializer() {
//		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
//		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
//
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		serializer.setObjectMapper(mapper);
//		return serializer;
//	}

//	/**
//	 * 配置缓存管理器
//	 * 需要注意的是 你在RedisTemplate<String, Object>中的配置的key，value序列化方法并不会生效
//	 * 需要在RedisCacheConfiguration中单独配置。
//	 *
//	 * @param redisConnectionFactory
//	 * @return
//	 */
//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//		// 生成一个默认配置，通过config对象即可对缓存进行自定义配置
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//		// 设置缓存的默认过期时间，也是使用Duration设置 （此处为缓存1分钟）
//		config = config.entryTtl(Duration.ofMinutes(1))
//				// 设置 key为string序列化
//				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//				// 设置value为json序列化
//				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()))
//				// 不缓存空值
//				.disableCachingNullValues();
//
//		// 设置一个初始化的缓存空间set集合
//		Set<String> cacheNames = new HashSet<>();
//		cacheNames.add("timeGroup");
//		cacheNames.add("user");
//		cacheNames.add("UUser");
//
//		// 对每个缓存空间应用不同的配置
//		Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
//		configMap.put("timeGroup", config);
//		// 该缓存空间，缓存时间120秒
//		configMap.put("user", config.entryTtl(Duration.ofSeconds(120)));
//		configMap.put("UUser", config.entryTtl(Duration.ofDays(3)));
//
//		// 使用自定义的缓存配置初始化一个cacheManager
//		RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
//				// 一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
//				.initialCacheNames(cacheNames)
//				.withInitialCacheConfigurations(configMap)
//				.build();
//		return cacheManager;
//	}

}
