package ygo.comn.controller.redis;

import io.netty.channel.Channel;
import ygo.comn.model.GlobalMap;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis工厂类
 * 负责生产和销毁Redis客户端
 *
 * @author Egan
 * @date 2018/6/16 18:55
 **/
public class RedisFactory {

    private static Map<InetSocketAddress, RedisClient> redisGroup = new HashMap<>();

    public static RedisClient getRedisforLobby(InetSocketAddress address){
        return getRedisClient(address, false);
    }

    public static RedisClient getRedisforDuel(InetSocketAddress address){
        return getRedisClient(address, true);
    }

    private static RedisClient getRedisClient(InetSocketAddress address, boolean isDuelServer){
        RedisClient redis = redisGroup.get(address);
        if(redis == null){
            redis = new RedisClient(isDuelServer);
            redisGroup.put(address, redis);
        }
        return redis;
    }

    public static void closeRedis(InetSocketAddress address){
        RedisClient redis = redisGroup.remove(address);
        if(redis != null)
            redis.close();
    }

    public static int redisCount(){
        return redisGroup.size();
    }
}
