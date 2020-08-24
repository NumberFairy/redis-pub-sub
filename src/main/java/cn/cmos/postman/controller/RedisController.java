package cn.cmos.postman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * TODO
 * @DES: 这里的Controller充当发布者 Publisher
 * @AUTHOR ZhaoChengcai
 * @DATE 2020/8/21
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    StringRedisTemplate template;

    @RequestMapping("/sendMessage")
    public String sendMessage(){
        for(int i=0;i<5;i++){
            template.convertAndSend("channel:test", String.format("我是消息{%d}号： %tT", i, new Date()));
        }
        return "ok";
    }

    @RequestMapping("/sendMessage2")
    public String sendMessage2(){
        template.convertAndSend("channel - 1", "中国之声");
        template.convertAndSend("channel - 2", "夜莺之声");
        return "ok2";
    }
}
