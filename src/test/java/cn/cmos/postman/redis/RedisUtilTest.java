package cn.cmos.postman.redis;

import cn.cmos.postman.PostmanApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @AUTHOR ZhaoChengcai
 * @DATE 2020/8/21
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostmanApplication.class})
public class RedisUtilTest {

    @Resource
    RedisUtil redisUtil;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    /**
     * @DES: redis -- String
     * @author ZhaoChengcai
     * @date 2020/8/21 11:38
     */
    @Test
    public void test1() {
        System.out.println(redisUtil);
        redisUtil.set("strName", "张三", 1000);
        redisUtil.set("strAge", 19, 1000);
        redisUtil.set("strGender", "女", 5000);
        System.out.println("strName:" + redisUtil.get("strName") + "--" + "strAge" + redisUtil.get("strAge") + "--strGender" + redisUtil.get("strGender"));
        System.out.println("strName:" + redisUtil.get("strName") + "--" + "strAge" + redisUtil.get("strAge") + "--strGender" + redisUtil.get("strGender") + "strName的过期时间：" + redisUtil.getExpire("strName"));
    }

    /**
     * @Des: redis -- List
     * @author ZhaoChengcai
     * @date 2020/8/21 11:56
     */
    @Test
    public void test2() {
        redisUtil.lSet("pig", "王子");
        redisUtil.lSet("pig", 5);
        redisUtil.lSet("pig", "female");
        System.out.println("List: Pig --" + redisUtil.getList("pig", 0, -1) + ", size:" + redisUtil.getListSize("pig"));
    }

    @Test
    public void test3() {
//        redisUtil.del("dog");
//        redisUtil.del("10");
//        redisUtil.del("10086");
//        redisUtil.del("age");
//        redisUtil.del("hs");
        System.out.println("删除完毕!");

    }

    @Test
    public void test4() {
        redisTemplate.opsForList().rightPush("list", 100);
        redisTemplate.opsForList().rightPush("list", "hello");
        redisTemplate.opsForList().rightPush("list", 3.14d);
        List<Object> list = redisTemplate.opsForList().range("list", 0, -1);
        for(Object o : list){
            System.out.println(o.toString());
        }
    }
}
