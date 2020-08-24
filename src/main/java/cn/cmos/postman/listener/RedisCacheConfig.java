package cn.cmos.postman.listener;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * TODO
 * 配置消息监听容器、监听适配器
 *
 * 消息队列配置 -- 订阅者
 * @AUTHOR ZhaoChengcai
 * @DATE 2020/8/21
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {
    /**
     * @DES: 配置消息监听容器；我们这里添加了两个消息监听适配器（可添加多个，这要看你想监听多少个Channel）
     *
     * @params factory
     * @params listenerAdapter
     * @params listenerAdapter2
     * @return RedisMessageListenerContainer
     * @author ZhaoChengcai
     * @date 2020/8/21 16:44
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter listenerAdapter, MessageListenerAdapter listenerAdapter2){
        // 设置工厂连接
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        // 可以添加多个 messageListener ，配置不同的交换机
        container.addMessageListener(listenerAdapter, new PatternTopic("channel - 1"));
        container.addMessageListener(listenerAdapter2, new PatternTopic("channel - 2"));
        return container;
    }
    
    /**
     *
     * @DES 绑定订阅者 和 接收监听的方法
     * @return  MessageListenerAdapter
     * @author ZhaoChengcai
     * @date 2020/8/21 16:02
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisReceiver receiver){
        return new MessageListenerAdapter(receiver, "onMessage");
    }

    /**
     *
     * @params 绑定订阅者 和 接收监听的方法
     * @return MessageListenerAdapter
     * @author ZhaoChengcai
     * @date 2020/8/21 16:03
     */
    @Bean
    MessageListenerAdapter listenerAdapter2(RedisReceiver receiver){
        return new MessageListenerAdapter(receiver, "onMessage2");
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory factory){
        return new StringRedisTemplate(factory);
    }
}
