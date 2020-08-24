package cn.cmos.postman.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类 <String, Object>
 */
@Component
public final class RedisUtil {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /*********************Common**********************/
    /**
     * 判断缓存是否失效
     * @param key 键
     * @param time 时间（秒）
     * @return
     */
    public boolean expire(String key, long time){
        try {
            if(time>0){
                redisTemplate.getExpire(key, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取缓存失效时间
     * @param key
     * @return 时间（秒）， 如果返回0， 表示永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在， false不存在
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传入多个值
     */
    public void del(String... key){
        if(key != null && key.length>0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /********************String**********************/

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存的放入
     * @param key 键
     * @param value 值
     * @return true放入成功，false放入失败
     */
    public boolean set(String key, Object value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入，并设置缓存时间
     * @param key 键
     * @param value 值
     * @param time 设置的缓存时间
     * @return
     */
    public boolean set(String key, Object value, long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 递增因子
     * @return
     */
    public long incre(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子delta必须要大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key
     * @param delta
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子delta必须要大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /********************Map**********************/

    /**
     * 获取hashkey的所有键值
     * @param key 键
     * @return
     */
    public Map<Object, Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * hashset
     * @param key
     * @param map
     * @return
     */
    public boolean hmset(String key, Map<Object, Object> map){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hashset 并设置过期时间
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key, Map<Object, Object> map, long time){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            if(time>0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张哈希表hash中放入数据，如果不存在就创建
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hset(String key, String item, Object value){
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张哈希表hash中放入数据，如果不存在就创建
     * @param key
     * @param item
     * @param value
     * @param time 如果已存在的hash表中有time值，这里会被替换
     * @return
     */
    public boolean hset(String key, String item, Object value, long time){
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if(time>0){
                expire(item, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中值（或多个值）
     * @param key 非null
     * @param item 可以是多个，但非null
     * @return
     */
    public void del(String key, Object... item){
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 ， 如果不存在就创建一个 ，并把新增后的值返回
     * @param key
     * @param value
     * @param by
     * @return
     */
    public double hincre(String key, String value, double by){
        return redisTemplate.opsForHash().increment(key, value, by);
    }

    /**
     * hash递减， 如果不存在就创建一个 ，并把递减后的值返回
     * @param key
     * @param value
     * @param by
     * @return
     */
    public double hdecre(String key, String value, double by){
        return redisTemplate.opsForHash().increment(key, value, -by);
    }

    /*******************Set**********************/

    /**
     * 根据 key 获取set中所有的值
     * @param key
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询，是否存在
     * @param value
     * @return
     */
    public boolean sHasKey(String key, Object value){
        try{
            return redisTemplate.opsForSet().isMember(key, value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key
     * @param values
     * @return
     */
    public long sSet(String key, Object... values){
        try{
            return redisTemplate.opsForSet().add(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 把数据放入set缓存，并设置过期时间
     * @param key
     * @param time
     * @param values
     * @return 返回成功放入数据的个数
     */
    public long sSetAndTime(String key, long time, Object... values){
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if(time>0){
                expire(key, time);
            }
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 返回set缓存的长度
     * @param key
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除指定set中值位values的
     * @param key
     * @param values
     * @return 删除个数
     */
    public long setRemove(String key, Object... values){
        try {
            return redisTemplate.opsForSet().remove(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /*******************List**********************/

    /**
     * 获取缓存list中指定起始位置的值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> getList(String key, long start, long end){
        try{
            return redisTemplate.opsForList().range(key, start, end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取缓存list的长度
     * @param key
     * @return
     */
    public long getListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据索引获取缓存list中的值
     * @param key
     * @param index
     * @return
     */
    public Object getValueByIndex(String key, long index){
        try {
           return redisTemplate.opsForList().index(key, index);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将指定的数据放入list缓存
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, Object value){
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将指定的数据放入list缓存,并设置国过期时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSet(String key, Object value, long time){
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if(time>0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量放入数据到缓存list
     * @param key
     * @param obs
     * @return
     */
    public boolean lsetAll(String key, List<Object> obs){
        try {
            redisTemplate.opsForList().rightPushAll(key, obs);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量放入数据到缓存list，并设置过期时间
     * @param key
     * @param obs
     * @return
     */
    public boolean lsetAllAndTime(String key, List<Object> obs, long time){
        try {
            redisTemplate.opsForList().rightPushAll(key, obs);
            if(time>0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key更新list中值
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lupdateByIndex(String key, long index, Object value){
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除缓存中count个值value
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lRemove(String key, long count, Object value){
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
