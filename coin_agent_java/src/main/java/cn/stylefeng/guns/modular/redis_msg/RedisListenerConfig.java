package cn.stylefeng.guns.modular.redis_msg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Redis消息监听配置
 */
//@Configuration
public class RedisListenerConfig
{
    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            MessageListenerAdapter createKlineReturn,
                                            MessageListenerAdapter createKlineReturnError,
                                            MessageListenerAdapter releaseFot
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        //可以添加多个 messageListener
        container.addMessageListener(listenerAdapter, new PatternTopic("index"));

        container.addMessageListener(releaseFot, new PatternTopic("releaseFot"));
        container.addMessageListener(createKlineReturn, new PatternTopic("createKlineReturn"));
        container.addMessageListener(createKlineReturnError, new PatternTopic("createKlineReturnError"));

        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param redisReceiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisReceiver redisReceiver) {
        System.out.println("消息适配器监听");
        return new MessageListenerAdapter(redisReceiver, "receiveMessage");
    }

    @Bean
    MessageListenerAdapter createKlineReturn(RedisReceiver createKlineReturn) {
        System.out.println("生成k线监听");
        return new MessageListenerAdapter(createKlineReturn, "createKlineReturn");
    }
    @Bean
    MessageListenerAdapter createKlineReturnError(RedisReceiver createKlineReturn) {
        System.out.println("生成k线报错监听");
        return new MessageListenerAdapter(createKlineReturn, "createKlineReturnError");
    }
     /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param redisReceiver
     * @return
     */
    @Bean
    MessageListenerAdapter releaseFot(RedisReceiver redisReceiver) {
        System.out.println("释放fot监听");
        return new MessageListenerAdapter(redisReceiver, "releaseFot");
    }


    /**使用默认的工厂初始化redis操作模板*/
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

}
