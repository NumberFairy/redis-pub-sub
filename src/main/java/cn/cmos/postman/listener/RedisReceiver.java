package cn.cmos.postman.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 接听对象 -- 订阅者
 * <p>
 * 继承MessageListener接口，可拿到消息体和Channel
 *
 * @AUTHOR ZhaoChengcai
 * @DATE 2020/8/21
 */
@Component
public class RedisReceiver implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println(new String(message.getBody()));
        System.out.println(new String(message.getChannel()));
    }
}
